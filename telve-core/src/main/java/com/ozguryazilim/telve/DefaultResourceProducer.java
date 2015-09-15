/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve;

import com.ozguryazilim.telve.auth.UserInfo;
import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.data.api.audit.CurrentUser;

/**
 * Telve uygulamaları içerisinde kullanılacak temel kaynakları üretir.
 *
 * @author Hakan Uygun
 */
@RequestScoped
public class DefaultResourceProducer {

    @Inject @Any
    private UserInfo userInfo;
    
    @Produces @Default
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    @Produces @CurrentUser
    public String currentUser() {
        return userInfo.getLoginName();
    }
 
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
