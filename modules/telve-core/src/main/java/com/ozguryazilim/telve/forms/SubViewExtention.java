package com.ozguryazilim.telve.forms;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

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
