/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.RelationshipQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Login olmuş ( session'a sahip ) kullanıcı için bilgiler döndürür.
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class ActiveUserLookup implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(ActiveUserLookup.class);
    
    private static final String USER_TYPE = "UserType"; 
    private static final String USER_GROUP = "UserGroup"; 
    
    private User activeUser;
    
    @Inject
    private Identity identity;
    
    @Inject
    private IdentityManager identityManager;
    
    @Inject
    private RelationshipManager relationshipManager;
    
    
    @Inject @Any
    private Instance<UserRoleResolver> userRoleResolvers;
    
    private List<Role> roles;
    
    @Inject @ActiveUserRoles
    private List<String> roleNames;
    
    private List<String> unifiedRoles;
    
     /**
     * Geriye login olan aktif kullanıcının userType bilgisini döndürür.
     * @return 
     */
    public String getActiveUserType(){
        Attribute<String> ut = getActiveUser().getAttribute(USER_TYPE);
        return ut == null ? "STANDART" : ut.getValue();
    }
    
    /**
     * Geriye login olan aktif kullanıcının userType bilgisini döndürür.
     * @return 
     */
    public String getActiveUserGroup(){
        Attribute<String> ut = getActiveUser().getAttribute(USER_GROUP);
        return ut == null ? "" : ut.getValue();
    }
    
    /**
     * Geriye Login olan kullanıcının User bilgisini döndürür.
     * @return 
     */
    public User getActiveUser(){
        
        if( activeUser == null ){
            //Eğer oturum açılmamış, identity yok ise geriye null döndürelim.
            if( identityManager == null || identity == null || identity.getAccount() == null ) return null;
            activeUser = identityManager.lookupIdentityById(User.class, identity.getAccount().getId());
        }
        
        return activeUser;
    } 
    
    
    public List<String> getUnifiedRoles() {
        if( unifiedRoles == null ){
            populateUnifiedRoles();
        }
        return unifiedRoles;
    }

    public void setUnifiedRoles(List<String> unifiedRoles) {
        this.unifiedRoles = unifiedRoles;
    }

    /*
    public List<Role> getRoles() {
        if( roles == null ){
            populateRoles();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }*/

    public List<String> getRoleNames() {
        if( roleNames == null ){
            populateRoles();
        }
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }
    
    
    
    /**
     * Kullanıcıya ait role, user id, group id, ve uygulamalardan gelebilecek örneğin organizasyon bilgilerini bir araya toparlar.
     * 
     * Farklı türler için birer harf kullanır : 
     * 
     * Reservd olanlar : 
     * U: User id
     * L: Login name
     * T: User Type
     * R: Role
     * G: Group
     * 
     * Örnek : 
     * U:telve 
     * R:superuser
     * G:Admins
     * 
     */
    public void populateUnifiedRoles(){
        unifiedRoles = new ArrayList<>();
        
        User u = getActiveUser();
        
        //Kullanıcının ID'si
        unifiedRoles.add("U:" + identity.getAccount().getId());
        unifiedRoles.add("L:" + u.getLoginName());
        unifiedRoles.add("T:" + getActiveUserType());
        
        //Kullanıcı rolleri
        for( String r : getRoleNames() ){
            unifiedRoles.add("R:" + r );
        }
        
        //TODO: Group'larda roller gibi toplanacak
        
        
        //Şimdide uygulamalardan gelenler toparlanıyor. Bir den fazla resolver olabilir.
        for ( UserRoleResolver urr : userRoleResolvers ){
            unifiedRoles.addAll( urr.getUnifiedRoles());
        }
        
        LOG.debug("UnifiedRoles : {}", unifiedRoles);
    }
    

    /**
     * Kullanıcıya ait roller toparlanıyor.
     */
    protected void populateRoles(){
        roles = new ArrayList<>();
        roleNames = new ArrayList<>();
        
        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
        query.setParameter(Grant.ASSIGNEE, identity.getAccount());
        List<Grant> result = query.getResultList();
        for (Grant grant : result) {
            roles.add(grant.getRole());
            roleNames.add(grant.getRole().getName());
        }
        
        //Eğer boşsa içine none ekliyoruz. Böylece IN içine konan sorgular hata vermeyecek.
        if( roleNames.isEmpty() ){
            roleNames.add("NONE");
        }
    }
    
}
