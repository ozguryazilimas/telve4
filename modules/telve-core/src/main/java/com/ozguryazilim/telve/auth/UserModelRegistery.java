/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sisteme tanımlanan UserModel eklentilerini tutar.
 * 
 * @author Hakan Uygun
 */
public class UserModelRegistery {
   
    
    private static final Logger LOG = LoggerFactory.getLogger(UserModelRegistery.class);
    
    private static final Map< String, UserModel> userModels = new HashMap<>();
    
    public static final String DEFAULT_USER_TYPE = "STANDART";
    public static final String SUPER_ADMIN_TYPE = "SUPERADMIN";
    
    private static final List<String> userTypes = new ArrayList<>();

    /**
     * Sisteme yeni bir user model meta datası ekler.
     * @param name rapor sınıfının EL adı
     * @param a 
     */
    public static void register( String name, UserModel a) {
        userModels.put( name, a);
        LOG.info("Regitered UserModel : {}", name);
        for( String s : a.userTypes() ){
            userTypes.add(s);
            LOG.info("Regitered UserType : {}", s);
        }
    }

    public static void registerUserType( String userType ) {
        userTypes.add(userType);
    }
    
    /**
     * User Model Meta data listesini döndürür.
     * @return 
     */
    public static Map< String, UserModel> getUserModelMap() {
        return userModels;
    }
    
    /**
     * Geriye UserModel isim listesini döndürür.
     * @return 
     */
    public static List< String > getUserModels() {
        return new ArrayList( userModels.keySet() );
    }
    
    
    /**
     * Geriye kullanıcı tip listesini döndürür.
     * @return 
     */
    public static List<String> getUserTypes() {
        return userTypes;
    }

    /**
     * Geriye varsayılan kullanıcı tipini döndürür.
     * @return 
     */
    public static String getDefaultUserType() {
        if( !userTypes.contains(DEFAULT_USER_TYPE)){
            userTypes.add(0, DEFAULT_USER_TYPE);
        }
        return DEFAULT_USER_TYPE;
    }

    
    
}
