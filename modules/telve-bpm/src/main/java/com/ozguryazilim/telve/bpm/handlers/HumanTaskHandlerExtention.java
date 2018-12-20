package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.CaseFormat;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

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
