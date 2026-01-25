package com.ozguryazilim.telve.messagebus.command.ui;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

/**
 * CommandEditor ile işaretlenmiş sınıfları toparlayıp Registery'e yerleştirir.
 * @author Hakan Uygun
 */
public class CommandEditorExtention implements Extension{
    
    /**
     * CommandEditor ile işaretli sınıfları bulup Registery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(CommandEditor.class) ProcessAnnotatedType<T> pat) {

        CommandEditor a = pat.getAnnotatedType().getAnnotation(CommandEditor.class);
        String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
        
        //name.to
        CommandEditorRegistery.register( name, a);
    }
    
}
