package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.CaseFormat;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

/**
 * ProcessHandler ile şaretlenmiş sınıfları tarayıp registery'e ekler.
 * @author Hakan Uygun
 */
public class ProcessHandlerExtention implements Extension{
    
    
    /**
     * Scans ProcessHandler annotated classes
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(ProcessHandler.class) ProcessAnnotatedType<T> pat) {
        ProcessHandler a = pat.getAnnotatedType().getAnnotation(ProcessHandler.class);
        
        String beanName = pat.getAnnotatedType().getJavaClass().getSimpleName();
        beanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, beanName);
        
        ProcessHandlerRegistery.register(beanName, a);
    }
}
