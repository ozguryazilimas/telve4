package com.ozguryazilim.telve.feature;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uygulama ayağa kalkarken Feature'leri toparlayıp FeatureRegistery'e yerleştirir.
 * 
 * Ayrıca yapılan kontrolleri de yapar.
 * 
 * @author Hakan Uygun
 */
public class FeatureExtention implements Extension{
    
    private static final Logger LOG = LoggerFactory.getLogger(FeatureExtention.class);
    
    /**
     * Feature ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(Feature.class) ProcessAnnotatedType<T> pat) {
        Feature a = pat.getAnnotatedType().getAnnotation(Feature.class);

        if( FeatureHandler.class.isAssignableFrom( pat.getAnnotatedType().getJavaClass()) ){
            Class<? extends FeatureHandler> clazz = (Class<? extends FeatureHandler>) pat.getAnnotatedType().getJavaClass();
            FeatureRegistery.register( a, clazz );
        } else {
            LOG.error("{} is not extends FeatureHandler. This Feature cannat be used.", pat.getAnnotatedType().getJavaClass().getName());
        }
        
    }
}
