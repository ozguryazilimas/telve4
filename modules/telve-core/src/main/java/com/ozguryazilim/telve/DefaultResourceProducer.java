package com.ozguryazilim.telve;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;

/**
 * Telve uygulamaları içerisinde kullanılacak temel kaynakları üretir.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
public class DefaultResourceProducer {

    /* FIXME: jakarta: bu method acaba artık gerekmiyor olabilir mi?
    @Produces @Default
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
         */
    
    @Produces
    @Default
    @ApplicationScoped
    public Cache<String, Object> produceObjectCache() {
        Cache<String, Object> cache= Caching.getCachingProvider().getCacheManager().createCache("TelveCache",
                new MutableConfiguration<String, Object>()
                    .setStoreByValue(false)
                    .setStatisticsEnabled(false)
                    .setManagementEnabled(false)
                    .setTypes(String.class, Object.class));
                    //.setWriteThrough(true)
                    //.setReadThrough(true)
                    //.setCacheLoaderFactory(FactoryBuilder.factoryOf( new OptionCacheLoader(new OptionRepository2(emf))))
                    //.setCacheWriterFactory(FactoryBuilder.factoryOf( new OptionCacheWriter(optionRepository))));
            //.setExpiry(CacheConfiguration.ExpiryType.MODIFIED, new Duration(TimeUnit.MINUTES, 10))
        //.setStoreByValue(false)
        //.build();
        //return procuceCacheManager().createCache("Option-Cache", null);
        
        
        return cache;
    }
    
}
