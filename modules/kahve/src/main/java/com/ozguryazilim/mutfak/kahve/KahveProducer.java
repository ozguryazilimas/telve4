package com.ozguryazilim.mutfak.kahve;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

/**
 *
 * @author haky
 */
@ApplicationScoped
public class KahveProducer {
    
    @Produces
    @Default
    @ApplicationScoped
    public Cache<String, KahveEntry> produceKahveCache( @com.ozguryazilim.mutfak.kahve.annotations.Kahve DataSource dataSource ) {
        //Önce kullanılacak store'u ayarlıyoruz.
        KahveStore.createInstance(dataSource);
        
        //Şimdide cache'i
        Cache<String, KahveEntry> cache= Caching.getCachingProvider().getCacheManager().createCache("KahveCache",
                new MutableConfiguration<String, KahveEntry>()
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true)
                    .setManagementEnabled(true)
                    .setTypes(String.class, KahveEntry.class)
                    .setWriteThrough(true)
                    .setReadThrough(true)
                    .setCacheLoaderFactory(FactoryBuilder.factoryOf( new KahveCacheLoader()))
                    .setCacheWriterFactory(FactoryBuilder.factoryOf( new KahveCacheWriter())));
            //.setExpiry(CacheConfiguration.ExpiryType.MODIFIED, new Duration(TimeUnit.MINUTES, 10))
        //.setStoreByValue(false)
        //.build();
        //return procuceCacheManager().createCache("Option-Cache", null);
        
        //OptionCache opcache = new OptionCache(cache, cacheRepository);
        //opcache.loadAll();
        return cache;
    }
    
    @Produces
    @Default
    public Kahve produceKahve(Cache<String, KahveEntry> kahveCache) {
        return new Kahve(false, kahveCache, "" );
    }
    
    
    @Produces
    @UserAware
    public Kahve produceKahveUserAware( Cache<String, KahveEntry> kahveCache, @UserAware String identity ) {
        return new Kahve(true, kahveCache, identity);
    }
}
