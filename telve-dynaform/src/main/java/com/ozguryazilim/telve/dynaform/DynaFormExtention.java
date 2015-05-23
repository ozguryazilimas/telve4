/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynaForm Deployer extention
 * @author Hakan Uygun
 */
public class DynaFormExtention  implements Extension{

    private static final Logger LOG = LoggerFactory.getLogger(DynaFormExtention.class);
    
    
    /**
     * DynaFormDeployer ile işaretli sınıfları bulup sisteme yükler.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(DynaFormDeployer.class) ProcessAnnotatedType<T> pat) {

        LOG.info("Form Deployer Extention");
        
        DynaFormDeployer a = pat.getAnnotatedType().getAnnotation(DynaFormDeployer.class);
        String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
        LOG.info("Form Deployer Extention : {}", name);
        //TODO: Burada aslında sınıfın doğru olup olmadığına da bir bakmak lazım.
        DynaFormManager.registerBuilder((Class<DynaFormBuilder>) pat.getAnnotatedType().getJavaClass());
        
    }
}
