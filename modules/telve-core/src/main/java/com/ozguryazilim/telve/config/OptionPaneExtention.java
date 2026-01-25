package com.ozguryazilim.telve.config;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

/**
 * OptionPane ile işaretli sınıfları tarar.
 * 
 * @author Hakan Uygun
 */
public class OptionPaneExtention implements Extension{

    /**
     * OptionPane ile işaretli sınıfları bulup OptionPaneRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(OptionPane.class) ProcessAnnotatedType<T> pat) {
        OptionPane a = pat.getAnnotatedType().getAnnotation(OptionPane.class);
        String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
        
        //name.to
        OptionPaneRegistery.register( name, a);
    }
}
