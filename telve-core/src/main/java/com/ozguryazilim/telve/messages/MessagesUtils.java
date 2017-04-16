/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messages;

import java.util.Map;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Uygulama içerisinde i18n message'larına ulaşmak için Utility sınıf.
 * @author Hakan Uygun
 */
public class MessagesUtils {
    
    
    public static String getMessage( String key ){
        return getMessages().get(key);
    }
    
    /**
     * Returns i18n message bundles
     * @return 
     */
    private static Map<String,String> getMessages(){
        return (Map<String, String>) BeanProvider.getContextualReference("messages", true );
    }
}
