/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Aktif kullanıcı ve ona ait bilgileri üretir.
 * 
 * Lazım olan Sorgular 
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class UserInfoProducer implements Serializable{
    
    private static final String USER_TYPE = "UserType"; 
    
//    private List<Role> roles;
    private List<String> roleNames;
    
//    @Inject
//    private Identity identity;
//    
//    @Inject
//    private UserInfo userInfo;
//    
//    @Inject
//    private RelationshipManager relationshipManager;
//
//    @Inject
//    private IdentityManager identityManager;
    
//    @Produces @SessionScoped @ActiveUserRoles
//    public List<String> getActiveUserRoleNames(){
//        if( roleNames == null ){
//            populateRoles();
//            roleNames = new ArrayList<>();
//            for( Role r : roles ){
//                roleNames.add(r.getName());
//            }
//            //Eğer boşsa içine none ekliyoruz. Böylece IN içine konan sorgular hata vermeyecek.
//            if( roleNames.isEmpty() ){
//                roleNames.add("NONE");
//            }
//        }
//        return roleNames;
//    }
    
    protected void populateRoles(){
//        roles = new ArrayList<>();
        
//        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
//        query.setParameter(Grant.ASSIGNEE, identity.getAccount());
//        List<Grant> result = query.getResultList();
//        for (Grant grant : result) {
//            roles.add(grant.getRole());
//        }
    }
    
    /**
     * UserAware Kahve için gerekli
     * @return 
     */
    @Produces @UserAware
    public String produceUserName(){
        //return userInfo.getLoginName();
        return "";
    }
    
    /**
     * PicketLink Identity üzerinden loginolan kullanıcının bilgileri session scope'a yerleştirir.
     * @return 
     */
    @Produces @SessionScoped @Named
    public UserInfo produceUserInfo(){
            UserInfo ui = new UserInfo();
//            
//            User user = identityManager.lookupIdentityById(User.class, identity.getAccount().getId());
//            
//            ui.setId( user.getId());
//            ui.setLoginName(user.getLoginName());
//            ui.setFirstName(user.getFirstName());
//            ui.setLastName(user.getLastName());
//            ui.setEmail(user.getEmail());
//            
//            Attribute<String> ut = user.getAttribute(USER_TYPE);
//            ui.setUserType( ut == null ? "STANDART" : ut.getValue());
            
            return ui;
    }
    
}
