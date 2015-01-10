/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.contact;

import com.google.common.base.CaseFormat;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haky
 */
public class ContactSourceExtention implements Extension{
    
    private static final Logger LOG = LoggerFactory.getLogger(ContactSourceExtention.class);
    
    /**
     * SubView ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(ContactSource.class) ProcessAnnotatedType<T> pat) {
        

        ContactSource a = pat.getAnnotatedType().getAnnotation(ContactSource.class);
        
        String beanName = pat.getAnnotatedType().getJavaClass().getSimpleName();
        beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, beanName);
        
        String name = a.name();
        
        if( name.isEmpty() ){
            name = beanName;
        }
        
        
        LOG.debug("Registered Contact Source {}", name );
        ContactSourceRegistery.register( name, beanName );
    }
}
