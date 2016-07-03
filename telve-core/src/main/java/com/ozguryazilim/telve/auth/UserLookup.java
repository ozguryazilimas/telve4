/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı bilgileri Lookup kontrol sınıfı.
 *
 * Altta bulanan farklı security realmlerinden bağımsız olarak kullanıcı bilgileri için arayüz dervisi sağlar.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class UserLookup implements Serializable{
    
    
    private static final Logger LOG = LoggerFactory.getLogger(UserLookup.class);
    
    @Inject
    private UserService userService;
    
    
    /**
     * Geriye sistemde tanımlı kullanıcı login isimlerini döndürür.
     * 
     * @return 
     */
    public List<String> getLoginNames(){
        return userService.getLoginNames();
    }
    
    public List<String> getUsers(){
        return getLoginNames();
    }
    

    public List<String> getUsers( String type, String group ){
        return userService.getUsersByTypeAndGroup(type, group);
    }
    
    /**
     * Kullanıcı tipine göre kullanıcı ( loginName ) listesi döndürür.
     * 
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir.
     * 
     * @param type
     * @return 
     */
    public List<String> getUsersByType( String type ){
        return userService.getUsersByType(type);
    }
    
    /**
     * Verilen Login Name'e sahip kullanıcının gerçek adını döndürür.
     * @param loginName ismi döndürülecek loginName
     * @return bulamazsa geriye null döndürür.
     */
    public String getUserName( String loginName ){
        return userService.getUserName(loginName);
    }
    
}
