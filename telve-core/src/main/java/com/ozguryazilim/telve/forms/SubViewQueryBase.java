/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.query.QueryControllerBase;
import javax.annotation.PostConstruct;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SubView'lar için detay soru control sınıfı.
 * 
 * @author Hakan Uygun
 * @param <E> Sorgu için kullanılacak Entity Sınıfı
 * @param <R> Sorgu sonuçları için kullanılacak ViewModel
 */
public abstract class SubViewQueryBase<E extends EntityBase,R extends ViewModel> extends QueryControllerBase<E,R>{


    private static final Logger LOG = LoggerFactory.getLogger(SubViewQueryBase.class);
    
    private E entity;

    
    @PostConstruct
    @Override
    public void init(){
        super.init();
        search();
    }
    
    /**
     * Geriye edit edilecek olan entity'i döndürür.
     * @return 
     */
    public E getEntity() {
        return entity;
    }

    /**
     * Edit edilen entity2i setler
     * @param entity 
     */
    public void setEntity(E entity) {
        this.entity = entity;
    }
    
    /**
     * Yeni bir entity oluşturur.
     */
    public void addItem(){
        try {
            entity = getRepository().createNew();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Error", ex);
        }
    }
    
    /**
     * Verilen id'li entity'i edit edilmek üzere hazırlar.
     * @param id 
     */
    public void editItem( Long id ){
        entity = getRepository().findBy(id);
    }
    
    /**
     * Verilen ID'li entity'i siler.
     * @param id
     */
    @Transactional
    public void removeItem( Long id ){
        onBeforeDelete();
        editItem(id);
        getRepository().remove(entity);
        search();
        onAfterDelete();
    }
    
    /**
     * Edit edilen entity'i saklar
     */
    @Transactional
    public void save(){
        onBeforeSave();
        getRepository().save(entity);
        search();
        onAfterSave();
    }
    
    /**
     * Saklama işleminden hemen önce çağrılır.
     */
    public void onBeforeSave(){
        //Bu method override edilmek için var.
    }
    
    /**
     * Saklama işleminden hemen sonra çağrılır.
     */
    public void onAfterSave(){
        //Bu method override edilmek için var.
    }
    
    /**
     * Silme işleminden hemen önce çağrılır.
     */
    public void onBeforeDelete(){
        //Bu method override edilmek için var.
    }
    
    /**
     * Silme işleminden hemen sonra çağrılır.
     */
    public void onAfterDelete(){
        //Bu method override edilmek için var.
    }
    
}
