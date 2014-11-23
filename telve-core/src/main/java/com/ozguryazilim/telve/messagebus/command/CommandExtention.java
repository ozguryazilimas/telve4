/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import com.google.common.base.CaseFormat;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Komut sistemi için CDI extention.
 * 
 * @Command ile işaretlenmiş sınıfları tarayıp toplar.
 * 
 * @author Hakan Uygun
 */
public class CommandExtention implements Extension{
    
    private static final Logger LOG = LoggerFactory.getLogger(CommandExtention.class);
    
    /**
     * SubView ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(CommandExecutor.class) ProcessAnnotatedType<T> pat) {
        

        CommandExecutor a = pat.getAnnotatedType().getAnnotation(CommandExecutor.class);
        
        String name = a.command().getName();
        String endpoint = a.endpoint();
        if( endpoint.isEmpty() ){
            endpoint = pat.getAnnotatedType().getJavaClass().getSimpleName();
            endpoint = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, endpoint);
            endpoint = "bean:" + endpoint;
        }
        
        LOG.debug("Registered Command {}", name );
        CommandRegistery.register( name, endpoint);
    }
    
}
