/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.sequence;

import com.ozguryazilim.telve.entities.Sequence;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Sequence durumlarını JPA ile saklar.
 * 
 * @author Hakan Uygun
 */
@RequestScoped
public class SequenceJpaStore implements SequenceStore{

    @Inject
    private EntityManager entityManager;
    
    @Override
    public Long findLastValue(String key) {
        Sequence sq = findSequence(key);
        return sq == null ? 0l : sq.getValue();
    }

    @Override
    public Long findNextValue(String key) {
        return findLastValue(key) + 1;
    }

    @Transactional @Override
    public Long getNextValue(String key) {
        Long l = findNextValue(key);
        saveValue(key, l);
        return l;
    }

    @Transactional @Override
    public void saveValue(String key, Long value) {
        
        Sequence sq = findSequence(key);
        
        if( sq == null ){
            sq = new Sequence(key, value);
            entityManager.persist(sq);
        } else {
            sq.setValue(value);
            entityManager.merge(sq);
        }
        entityManager.flush();
    }
    
    /**
     * Verilen key ile ilgili kayıt varsa bulur.
     * @param key
     * @return 
     */
    protected Sequence findSequence( String key ){
        List<Sequence> ls = entityManager.createQuery("select c from Sequence c where key = :key")
                .setParameter("key", key)
                .getResultList();
        
        if( ls.isEmpty()) return null;
        
        return ls.get(0);
    }
    
}
