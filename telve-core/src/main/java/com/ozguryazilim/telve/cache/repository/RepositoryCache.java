/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.cache.repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;

/**
 * JCache araüzünü üzerinden proxy sınıf.
 * 
 * Cache'e gönderilen entityleri aynı zamanda veri tabanına da kaydeder.
 * JCache writer ve loader'lar JPA/EntityMAnager ile arıza yaptı o yüzden :(
 * 
 * @author Hakan Uygun
 */
public abstract class RepositoryCache<K, V > implements Cache<K,V>{

    private Cache<K,V> cache;
    private AbstractCacheEntityRepository<K, V> repository;

    public RepositoryCache(Cache<K, V> cache, AbstractCacheEntityRepository<K, V> repository) {
        this.cache = cache;
        this.repository = repository;
    }
    
    
    
    @Override
    public V get(K k) {
        //Önce cache'de mevcut mu diye bak.
        V v = cache.get(k);
        
        //Eğer değilse veri tabanına bir bakalım
        if( v == null ){
            v = repository.find(k);
            if( v != null ){
                //veri tabanında bulduk cache'e koyalım
                cache.put(k, v);
            }
        }
        
        return v;
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> set) {
        
        Map<K, V> map = new HashMap<>();
        
        for( K k : set ){
            V v = get(k);
            if( v != null ){
                map.put(k, v);
            }
        }
        
        
        return map;
    }

    @Override
    public boolean containsKey(K k) {
        //Bu işlemden çok emin değilim. Veri tabanına bir bakmak lazım mı diye?
        return cache.containsKey(k);
    }

    @Override
    public void loadAll(Set<? extends K> set, boolean bln, CompletionListener cl) {
        //TODO: Bu işlem için ne yapmalı acaba?
        cache.loadAll(set, bln, cl);
    }

    @Override
    public void put(K k, V v) {
        //Önce veri tabanına
        repository.save(k, v);
        //ardından cache' koyalım
        cache.put(k, v);
    }

    @Override
    public V getAndPut(K k, V v) {
        put(k, v);
        return v;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        
        for( Map.Entry<? extends K, ? extends V> e : map.entrySet()){
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean putIfAbsent(K k, V v) {
        V vv = get(k);
        if( vv == null ){
            put(k, v);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean remove(K k) {
        repository.remove(k);
        return cache.remove(k);
    }

    @Override
    public boolean remove(K k, V v) {
        repository.remove(k, v);
        return cache.remove(k, v);
    }

    @Override
    public V getAndRemove(K k) {
        V v = get(k);
        remove(k, v);
        return v;
    }

    @Override
    public boolean replace(K k, V v, V v1) {
        //TODO: Bunlardan emin değilim.
        return cache.replace(k, v, v1);
    }

    @Override
    public boolean replace(K k, V v) {
        //TODO: Bunlardan emin değilim.
        return cache.replace(k, v);
    }

    @Override
    public V getAndReplace(K k, V v) {
        //TODO: Bunlardan emin değilim.
        return cache.getAndReplace(k, v);
    }

    @Override
    public void removeAll(Set<? extends K> set) {
        for( K k : set ){
            remove(k);
        }
    }

    @Override
    public void removeAll() {
        //TODO: Veri tabanını da silmeli mi?
        cache.removeAll();
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public <C extends Configuration<K, V>> C getConfiguration(Class<C> type) {
        return cache.getConfiguration(type);
    }

    @Override
    public <T> T invoke(K k, EntryProcessor<K, V, T> ep, Object... os) throws EntryProcessorException {
        return cache.invoke(k, ep, os);
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> set, EntryProcessor<K, V, T> ep, Object... os) {
        return cache.invokeAll(set, ep, os);
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public CacheManager getCacheManager() {
        return cache.getCacheManager();
    }

    @Override
    public void close() {
        cache.close();
    }

    @Override
    public boolean isClosed() {
        return cache.isClosed();
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return cache.unwrap(type);
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> celc) {
        cache.registerCacheEntryListener(celc);
    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> celc) {
        cache.deregisterCacheEntryListener(celc);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return cache.iterator();
    }
    
    public Cache<K,V> getWrappedCache(){
        return cache;
    }
    
    public AbstractCacheEntityRepository<K, V> getWrappedRepository(){
        return repository;
    }
}
