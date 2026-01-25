package com.ozguryazilim.telve.contact;

import com.google.common.base.CaseFormat;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;
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
