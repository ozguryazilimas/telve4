package com.ozguryazilim.telve.forms;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

/**
 * SubView ile işaretlenmiş sınıfları toplayıp SubViewRegistery'e yerleştirir.
 *
 * @author Hakan Uygun
 */
public class SubViewExtention implements Extension {

    /**
     * SubView ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(SubView.class) ProcessAnnotatedType<T> pat) {
        SubView a = pat.getAnnotatedType().getAnnotation(SubView.class);
        SubViewRegistery.register(a);
    }
    
    /**
     * Register edilmiş SubView'lar sıralar.
     * @param event
     * @param manager 
     */
    void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
        
        SubViewRegistery.sort();
    }
}
