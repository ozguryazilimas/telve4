package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.CaseFormat;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

/**
 *
 * @author Hakan Uygun
 */
public class HumanTaskHandlerExtention implements Extension{
    
    /**
     * Scans HumanTaskHandler annotated classes.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(HumanTaskHandler.class) ProcessAnnotatedType<T> pat) {
        HumanTaskHandler a = pat.getAnnotatedType().getAnnotation(HumanTaskHandler.class);
        
        String beanName = pat.getAnnotatedType().getJavaClass().getSimpleName();
        beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, beanName);
        
        HumanTaskHandlerRegistery.register(beanName, a);
    }
}
