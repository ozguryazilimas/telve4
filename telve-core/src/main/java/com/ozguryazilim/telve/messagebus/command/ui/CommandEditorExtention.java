/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

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
        String category =pat.getAnnotatedType().getAnnotation(CommandEditor.class).category();
        
        //name.to
        CommandEditorRegistery.register( name, a , category);
    }
    
}
