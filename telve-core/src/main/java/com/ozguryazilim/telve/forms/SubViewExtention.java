/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        System.out.println("SubView Registery Çalıştı");

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
