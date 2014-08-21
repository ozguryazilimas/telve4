/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.dashboard;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dashlet ile işaretli sınıfları tarar.
 * 
 * @author Hakan Uygun
 */
public class DashletExtention implements Extension{
    
    private static final Logger LOG = LoggerFactory.getLogger(DashletExtention.class);
    
    /**
     * Dashlet ile işaretli sınıfları bulup DashletRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(Dashlet.class) ProcessAnnotatedType<T> pat) {
        LOG.info("Dashlet Registery Çalıştı");

        Dashlet a = pat.getAnnotatedType().getAnnotation(Dashlet.class);
        
        //Dashlet deactive edilmemişse register ediliyor.
        String s = pat.getAnnotatedType().getJavaClass().getName() + ".deactive";
        String c = ConfigResolver.getProjectStageAwarePropertyValue( s, "false");
        
        if( !"true".equals(c)){
            String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
            DashletRegistery.register( name, a);
        } else {
            LOG.info("Inactive Dashlet: {}", pat.getAnnotatedType().getJavaClass().getName());
        }
    }
    
}
