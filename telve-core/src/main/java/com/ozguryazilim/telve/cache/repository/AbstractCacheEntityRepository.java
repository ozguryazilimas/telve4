/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.cache.repository;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * JCache önünde saklanması gereken entityleri yönetmek için taban repository sınıfı.
 * 
 * @author Hakan Uygun
 * @param <PK> Primary Key sınıfı
 * @param <E> Entity Sınıfı
 */
public abstract class AbstractCacheEntityRepository<PK, E> {

    @Inject
    private EntityManager em;
    
    protected abstract Class<E> getEntityClass();
    
    /**
     * Verilen primary key ile verilen değeri veri tabanına yazar.
     * @param pk
     * @param e
     * @return 
     */
    public E save( PK pk, E e ){
        E ee = (E) em.find( e.getClass(), pk );
        
        if( ee == null ){
            em.persist(e);
        } else {
            e = em.merge(e);
        }
        
        return e;
    }
    
    /**
     * Verilen pk değerine sahip kaydı veri tabanından siler.
     * @param pk 
     */
    public void remove( PK pk ){
        E e = find(pk);
        if( e != null ){
            em.remove(e);
        }
    }
    
    /**
     * Verilen key ve değere göre silme yapar.
     * @param pk
     * @param e 
     */
    public void remove( PK pk, E e ){
        em.remove(e);
    }
    
    /**
     * Verilen pk ile ilgili kaydı bulur. Bulamazsa null döner.
     * @param pk
     * @return 
     */
    public E find( PK pk ){
        return em.find(getEntityClass(), pk);
    }
    
    public List<E> findAll(){
        return em.createQuery("select c from " + getEntityClass().getName() + " c ").getResultList();
    }
}
