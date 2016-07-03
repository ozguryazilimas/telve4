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
    

    
    /**
     * UserAware Kahve için gerekli
     * @return 
     */
    @Produces @UserAware
    public String produceUserName(){
        //return userInfo.getLoginName();
        return "";
    }
    
    
}
