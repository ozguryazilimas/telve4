package com.ozguryazilim.telve.contact;

import java.util.HashMap;
import java.util.Map;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haky
 */
public class ContactSourceRegistery {

    private static final Logger LOG = LoggerFactory.getLogger(ContactSourceRegistery.class);
    
    private static final Map<String,String> sources = new HashMap<>();
    
    /**
     * Sisteme yeni bir contact source tanımlar.
     *
     * @param name source sınıfının adı
     * @param beanName cdiBean Name;
     */
    public static void register(String name, String beanName) {
        sources.put(name, beanName);
    }
    
    
    /**
     * Verilen isimli contactSource'un CDI instance'nı döndürür.
     * @param name
     * @return 
     */
    public static AbstractContactSource getContactSource( String name ){
        String beanName = sources.get(name);
        if( beanName != null ){
            return  (AbstractContactSource) BeanProvider.getContextualReference(beanName);
        }
        return null;
    } 
}
