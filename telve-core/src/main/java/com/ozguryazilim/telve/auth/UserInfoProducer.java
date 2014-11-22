/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.query.RelationshipQuery;

/**
 * Aktif kullanıcı ve ona ait bilgileri üretir.
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class UserInfoProducer implements Serializable{
    
    private List<Role> roles;
    private List<String> roleNames;
    
    @Inject
    private Identity identity;
    
    @Inject
    private RelationshipManager relationshipManager;
    
    @Produces @SessionScoped @ActiveUserRoles
    public List<Role> getActiveUserRoles(){
        if( roles == null ){
            populateRoles();
        }
        return roles;
    }
    
    @Produces @SessionScoped @ActiveUserRoles
    public List<String> getActiveUserRoleNames(){
        if( roleNames == null ){
            populateRoles();
            roleNames = new ArrayList<>();
            for( Role r : roles ){
                roleNames.add(r.getName());
            }
            //Eğer boşsa içine none ekliyoruz. Böylece IN içine konan sorgular hata vermeyecek.
            if( roleNames.isEmpty() ){
                roleNames.add("NONE");
            }
        }
        return roleNames;
    }
    
    protected void populateRoles(){
        roles = new ArrayList<>();
        
        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
        query.setParameter(Grant.ASSIGNEE, identity.getAccount());
        List<Grant> result = query.getResultList();
        for (Grant grant : result) {
            roles.add(grant.getRole());
        }
    }
    
    /**
     * UserAware Kahve için gerekli
     * @return 
     */
    @Produces @UserAware
    public String produceUserName(){
        return identity.getAccount().getId();
    }
    
}
