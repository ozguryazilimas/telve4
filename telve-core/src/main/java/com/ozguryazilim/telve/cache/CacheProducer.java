/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.cache;

import com.ozguryazilim.telve.entities.Option;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Sistemde kullanılacak Chache işleri için gerekli Bean'leri üretir.
 *
 * @author Hakan Uygun
 */
@RequestScoped
public class CacheProducer {

    @Inject 
    private OptionCacheRepository cacheRepository;
    
    @PersistenceUnit(unitName = "cacheStore")
    EntityManagerFactory emf;
    
    @Produces
    @ApplicationScoped
    public CacheManager procuceCacheManager() {
        return Caching.getCachingProvider().getCacheManager();
    }

    @Produces
    @Default
    @ApplicationScoped
    public Cache<String, Option> produceOptionCache() {
        Cache<String, Option> cache= Caching.getCachingProvider().getCacheManager().createCache("OpCache",
                new MutableConfiguration<String, Option>()
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true)
                    .setManagementEnabled(true)
                    .setTypes(String.class, Option.class));
                    //.setWriteThrough(true)
                    //.setReadThrough(true)
                    //.setCacheLoaderFactory(FactoryBuilder.factoryOf( new OptionCacheLoader(new OptionRepository2(emf))))
                    //.setCacheWriterFactory(FactoryBuilder.factoryOf( new OptionCacheWriter(optionRepository))));
            //.setExpiry(CacheConfiguration.ExpiryType.MODIFIED, new Duration(TimeUnit.MINUTES, 10))
        //.setStoreByValue(false)
        //.build();
        //return procuceCacheManager().createCache("Option-Cache", null);
        
        OptionCache opcache = new OptionCache(cache, cacheRepository);
        opcache.loadAll();
        return opcache;
    }

    @Produces
    @Default
    @ApplicationScoped
    public Cache<String, Object> produceObjectCache() {
        Cache<String, Object> cache= Caching.getCachingProvider().getCacheManager().createCache("ObCache",
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
