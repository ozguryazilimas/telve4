/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.Cache;
import javax.cache.integration.CacheWriter;
import javax.cache.integration.CacheWriterException;

/**
 * JCache için writer implementasyonu. 
 * 
 * Kahve Bileşenlerini DB'ye yazar.
 * 
 * @author Hakan Uygun
 */
public class KahveCacheWriter implements CacheWriter<String, KahveEntry>, Serializable{

    @Override
    public void write(Cache.Entry<? extends String, ? extends KahveEntry> entry) throws CacheWriterException {
        try {
            KahveStore.getInstance().save(entry.getKey(), entry.getValue());
        } catch (SQLException ex) {
            Logger.getLogger(KahveCacheWriter.class.getName()).log(Level.SEVERE, null, ex);
            throw new CacheWriterException("DB Exception", ex);
        }
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends String, ? extends KahveEntry>> entries) throws CacheWriterException {
        // Retrieve the iterator to clean up the collection from
        // written keys in case of an exception
        Iterator<Cache.Entry<? extends String, ? extends KahveEntry>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            // Write entry using dao
            write(iterator.next());
            // Remove from collection of keys
            iterator.remove();
        }
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        if (!(key instanceof String)) {
            throw new CacheWriterException("Illegal key type");
        }

        try {
            KahveStore.getInstance().delete((String) key);
        } catch (SQLException ex) {
            Logger.getLogger(KahveCacheWriter.class.getName()).log(Level.SEVERE, null, ex);
            throw new CacheWriterException("DB Exception", ex);
        }
    }

    @Override
    public void deleteAll(Collection<?> keys) throws CacheWriterException {
        // Retrieve the iterator to clean up the collection from
        // written keys in case of an exception
        Iterator<?> iterator = keys.iterator();
        while (iterator.hasNext()) {
            // Write entry using dao
            delete(iterator.next());
            // Remove from collection of keys
            iterator.remove();
        }
    }
    
}
