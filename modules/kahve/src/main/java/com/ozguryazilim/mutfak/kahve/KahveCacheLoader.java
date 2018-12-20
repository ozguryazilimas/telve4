package com.ozguryazilim.mutfak.kahve;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;

/**
 * JCache Loader implementasyonu.
 * 
 * Kahve içeriğini okur.
 * 
 * @author Hakan Uygun
 */
public class KahveCacheLoader implements CacheLoader<String, KahveEntry>, Serializable{

    @Override
    public KahveEntry load(String k) throws CacheLoaderException {
        return KahveStore.getInstance().load(k);
    }

    @Override
    public Map<String, KahveEntry> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
        Map<String, KahveEntry> map = new HashMap<>();
        for( String k : keys ){
            Object o = load(k);
            if( o != null ){
                map.put(k, load(k));
            }
        }
        return map;
    }
    
}
