/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.cache;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

/**
 * PrimeFaces p:cache için JCache Provider
 *
 * @author Hakan Uygun
 */
public class JCacheProvider implements CacheProvider {

    public JCacheProvider() {
    }

    protected Cache<String, Object> getReigon(String region) {

        Cache<String, Object> cache = Caching.getCachingProvider().getCacheManager().getCache("PF-" + region, String.class, Object.class);
        if (cache == null) {
            cache = Caching.getCachingProvider().getCacheManager().createCache("PF-" + region,
                    new MutableConfiguration<String, Object>()
                    .setStoreByValue(true)
                    .setStatisticsEnabled(false)
                    .setManagementEnabled(false)
                    .setTypes(String.class, Object.class)
                    .setWriteThrough(true)
                    .setReadThrough(true));
        }

        return cache;
    }

    @Override
    public Object get(String region, String key) {
        Cache<String, Object> cache = getReigon(region);

        return cache.get(key);
    }

    @Override
    public void put(String region, String key, Object value) {
        Cache<String, Object> cache = getReigon(region);
        cache.put(key, value);
    }

    @Override
    public void remove(String region, String key) {
        Cache<String, Object> cache = getReigon(region);
        cache.remove(key);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
