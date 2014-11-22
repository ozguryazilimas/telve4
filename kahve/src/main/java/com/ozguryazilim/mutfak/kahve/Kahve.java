/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.cache.Cache;

/**
 * Kullanıcı Durumuna Duyarlı Key-Value Store.
 *
 * JCache üzerinden çalışır.
 *
 * @author Hakan Uygun
 */
public class Kahve implements Serializable {

    private boolean userAware;

    private Cache<String, KahveEntry> store;

    private String identity;

    public Kahve(boolean userAware, Cache<String, KahveEntry> store, String identity) {
        this.userAware = userAware;
        this.store = store;
        this.identity = identity;
    }

    /**
     * Kullanıcıya duyarlı arama yapar.
     *
     * Eğer kullanıcı için bulamazsa bu sefer sistem seviyesinde arama yapar.
     *
     * @param key
     * @return
     */
    public KahveEntry get(String key) {
        //İki kere kontrol yapmaması için
        if (userAware) {
            KahveEntry result = store.get(getUserAwareKey(key));
            return result == null ? store.get(key) : result;
        } else {
            return store.get(key);
        }
    }
    
    
    public KahveEntry get( KahveCriteria criteria ) {
        
        for( String key : criteria.getKeys(identity)){
            System.out.println("Search Key : " + key);
            System.out.println("Identity : " + identity);
            KahveEntry result = store.get(key);
            if( result != null ) return result;
        }
        
        if( criteria.hasDefaultValue() ){
            put( criteria.getScopeKey(identity), criteria.getDefaultValue());
            return criteria.getDefaultValue();
        }
        
        return null;
    }
    

    public KahveEntry get(String key, KahveEntry defaultVal) {
        KahveEntry ke = get(key);

        if (ke == null) {
            ke = defaultVal;
            put(key, ke);
        }

        return ke;
    }
    
    public KahveEntry get(String key, String defaultVal) {
        return get(key, new KahveEntry(defaultVal));
    }
    
    public KahveEntry get(String key, Integer defaultVal) {
        return get(key, new KahveEntry(defaultVal));
    }
    
    public KahveEntry get(String key, Long defaultVal) {
        return get(key, new KahveEntry(defaultVal));
    }
    
    public KahveEntry get(String key, Boolean defaultVal) {
        return get(key, new KahveEntry(defaultVal));
    }
    
    public KahveEntry get(String key, BigDecimal defaultVal) {
        return get(key, new KahveEntry(defaultVal));
    }
    
    public KahveEntry get(String key, Date defaultVal) {
        return get(key, new KahveEntry(defaultVal));
    }

    public KahveEntry get(KahveKey key) {
        return get(key.getKey(), key.getDefaultValue());
    }

    /**
     * Kullanıcıya duyarlı put yapar.
     *
     * @param key
     * @param entry
     */
    public void put(String key, KahveEntry entry) {
        store.put(getUserAwareKey(key), entry);
    }

    public void put(String key, String value) {
        put( key, new KahveEntry(value));
    }

    public void put(String key, Integer value) {
        put( key, new KahveEntry(value));
    }
    
    public void put(String key, Long value) {
        put( key, new KahveEntry(value));
    }

    public void put(String key, Date value) {
        put( key, new KahveEntry(value));
    }

    public void put(String key, Boolean value) {
        put(key, new KahveEntry(value));
    }

    public void put(String key, BigDecimal value) {
        put(key, new KahveEntry(value));
    }

    public void put(String key, Enum value) {
        put(key, new KahveEntry(value));
    }

    public void put(KahveKey key, KahveEntry value) {
        put( key.getKey(), value );
    }
    
    public void put(KahveKey key, String value) {
        put( key, new KahveEntry(value));
    }
    
    public void put(KahveKey key, Integer value) {
        put( key, new KahveEntry(value));
    }
    
    public void put(KahveKey key, Long value) {
        put( key, new KahveEntry(value));
    }

    public void put(KahveKey key, Date value) {
        put( key, new KahveEntry(value));
    }

    public void put(KahveKey key, Boolean value) {
        put(key, new KahveEntry(value));
    }

    public void put(KahveKey key, BigDecimal value) {
        put(key, new KahveEntry(value));
    }

    public void put(KahveKey key, Enum value) {
        put(key, new KahveEntry(value));
    }
    
    
    /**
     * Kullanıcıya duyarlı silme yapar.
     *
     * @param key
     */
    public void remove(String key) {
        store.remove(getUserAwareKey(key));
    }

    protected String getUserAwareKey(String key) {
        System.out.println("Identity : " + identity);

        return userAware ? identity + "::" + key : key;
    }

    /**
     * Kullanıcı duyarlı olup olmadığı
     *
     * @return
     */
    public boolean isUserAware() {
        return userAware;
    }

    /**
     * Kullanıcı duyarlı çalışıp çalışmayacağını setler
     *
     * @param userAware
     */
    public void setUserAware(boolean userAware) {
        this.userAware = userAware;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Cache<String, KahveEntry> getStore() {
        return store;
    }

}
