/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Sorgular View Control Sınıfları için taban sınıf.
 * 
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <R> Result sınıfı
 */
public abstract class QueryControllerBase<E extends EntityBase,R extends ViewModel> implements Serializable{
    
    /**
     * Sorgu bilgilerini tutan sınıf
     */
    private QueryDefinition<E, R> queryDefinition = new QueryDefinition<>();
    
    //FIXME: Aslında R tipinde olmalı
    private List<E> entityList = Collections.EMPTY_LIST;
    
    protected E selectedItem;
    protected E[] selectedItems;
    
    /**
     * GUI için sorgu özellikleri tanımlanır.
     * @param queryDefinition 
     */
    protected abstract void buildQueryDefinition( QueryDefinition<E, R> queryDefinition );
    
    protected abstract RepositoryBase<E,R> getRepository();
    
    @PostConstruct
    public void init(){
        buildQueryDefinition(queryDefinition);
    }

    
    public QueryDefinition<E, R> getQueryDefinition() {
        return queryDefinition;
    }

    protected void decorateFilters( Criteria<E,E> criteria ){
        for( Filter<E, ?> f : queryDefinition.getFilters() ){
            f.decorateCriteria(criteria);
        }
    }
    
    /**
     * Sıralamaları ekler.
     * @param criteria 
     */
    protected void decorateOrders( Criteria<E,E> criteria ){
        //FIXME: İçeriği yazılacak
    }
    
    /**
     * Alt sınıflar bu methodu override ederek sorguya yeni şeyler ekleyebilirler.
     * @param criteria 
     */
    protected void decorateCriteria( Criteria<E,E> criteria ){
        
    }
    
    /**
     * Reporsitory üzerinden sorgu düzenler.
     * 
     * repositoryden sorgu alır ve üzerinde GUI'den gelenleri ekler.
     * 
     * @return 
     */
    protected List<E> executeCriteria(){
        Criteria<E,E> criteria = getRepository().browseCriteria();
        
        decorateFilters( criteria );
        
        decorateOrders( criteria );
        
        //Kullanıcının ekleyeceği şeyler varsa eklesin.
        decorateCriteria(criteria);
        
        if( queryDefinition.getResultLimit() > 0 ){
            return criteria.createQuery().setMaxResults(queryDefinition.getResultLimit()).getResultList();
        }
        
        return criteria.getResultList();
    }
    
    public void search(){
        entityList = executeCriteria();
    }

    public List<E> getEntityList() {
        return entityList;
    }

    public E getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(E selectedItem) {
        this.selectedItem = selectedItem;
    }

    public E[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(E[] selectedItems) {
        this.selectedItems = selectedItems;
    }
    
    
    
}
