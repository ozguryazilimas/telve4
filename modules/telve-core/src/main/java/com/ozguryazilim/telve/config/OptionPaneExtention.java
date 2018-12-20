package com.ozguryazilim.telve.config;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

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
