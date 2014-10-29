/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.google.common.base.CaseFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.deltaspike.core.api.provider.BeanProvider;
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
    
    private static final String DEFAULT_USER_TYPE = "STANDART";
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
     * Geriye extender sınıfların CDI Bileşen listesini döndürür.
     * @return 
     */
    public static List<AbstractIdentityHomeExtender> getExtenders() {
        List<AbstractIdentityHomeExtender> result = new ArrayList<>();
        for( String s : userModels.keySet() ){
            //Sınıf ismini EL ismi haline getiriyoruz.
            String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, s);
            result.add(BeanProvider.getContextualReference(name, true, AbstractIdentityHomeExtender.class));
        }
                
        return result;
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
