/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserModel eklentilerini scan ederek toparlar.
 * 
 * @author Hakan Uygun
 */
public class UserModelExtention implements Extension{
    private static final Logger LOG = LoggerFactory.getLogger(UserModelExtention.class);
    
    /**
     * UserModel ile işaretlenmiş sınıfları UserModelRegistery'e kaydeder.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(UserModel.class) ProcessAnnotatedType<T> pat) {
        LOG.info("UserModel Registery Scanner");

        UserModel a = pat.getAnnotatedType().getAnnotation(UserModel.class);
        String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
        
        UserModelRegistery.register(name, a);
    }
    
}
