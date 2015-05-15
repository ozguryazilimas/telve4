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
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.basic.User;

/**
 * Kullanıcı bilgileri Lookup kontrol sınıfı.
 * 
 * PicketLink IdentityManager'ı üzerinden kullanıcı bilgileri döndürür.
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class UserLookup implements Serializable{
    
    
    @Inject
    private IdentityManager identityManager;
    
    /**
     * Geriye sistemde tanımlı kullanıcı login isimlerini döndürür.
     * 
     * @return 
     */
    public List<String> getLoginNames(){
        
        List<String> result = new ArrayList<>();
        List<User> ls = getUsers();
        
        for( User u : ls ){
            result.add(u.getLoginName());
        }
        
        return result;
    }
    
    /**
     * Geriye sistemde tanımlı kullanıcı isimlerini döndürür.
     * 
     * @return 
     */
    public List<User> getUsers(){
        return identityManager.createIdentityQuery(User.class).getResultList();
    }
    
    /**
     * Verilen Login Name'e sahip kullanıcının gerçek adını döndürür.
     * @param loginName ismi döndürülecek loginName
     * @return bulamazsa geriye null döndürür.
     */
    public String getUserName( String loginName ){
        List<User> ls = identityManager.createIdentityQuery(User.class)
                .setParameter(User.LOGIN_NAME, loginName)
                .getResultList();
        
        if( !ls.isEmpty() ){
            return ls.get(0).getFirstName() + " " + ls.get(0).getLastName();
        }
        
        return null;
    }
    
    /**
     * Verilen ID'e sahip kullanıcının gerçek adını döndürür.
     * @param id ismi döndürülecek kullanıcı PL id'si
     * @return bulamazsa geriye null döndürür.
     */
    public String getUserNameById( String id ){
        User u = identityManager.lookupIdentityById(User.class, id);
        
        if( u != null ){
            return u.getFirstName() + " " + u.getLastName();
        }
        
        return null;
    }
    
}
