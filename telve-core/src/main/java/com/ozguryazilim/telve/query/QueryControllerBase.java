/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.query.filters.Filter;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.query.columns.Column;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sorgular View Control Sınıfları için taban sınıf.
 * 
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <R> Result sınıfı
 */
public abstract class QueryControllerBase<E extends EntityBase,R extends ViewModel> implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(QueryControllerBase.class);
    
    /**
     * Sorgu bilgilerini tutan sınıf
     */
    private QueryDefinition<E, R> queryDefinition = new QueryDefinition<>();
    
    //FIXME: Aslında R tipinde olmalı
    private List<E> entityList = Collections.EMPTY_LIST;
    
    protected E selectedItem;
    protected E[] selectedItems;
    
    //Sorguyu saklamak için kullanılacak değerler.
    private String queryName;
    private Boolean defaultQuery = false;
    private Boolean personal = true;

    @Inject @UserAware
    Kahve kahve;
    
    /**
     * GUI için sorgu özellikleri tanımlanır.
     * @param queryDefinition 
     */
    protected abstract void buildQueryDefinition( QueryDefinition<E, R> queryDefinition );
    
    protected abstract RepositoryBase<E,R> getRepository();
    
    @PostConstruct
    public void init(){
        buildQueryDefinition(queryDefinition);
        loadDefaultQuery();
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
    
    public void save(){
        
        QueryModel model = new QueryModel();
        model.setName(queryName);
        model.setResultLimit(queryDefinition.getResultLimit());
        model.setRowLimit(queryDefinition.getRowLimit());
        
        for( Column c : queryDefinition.getColumns()){
            model.getColumns().add(c.getName());
        }
        
        for( Filter c : queryDefinition.getFilters()){
            model.getFilters().add(c.getAttribute().getName());
            model.getFilterValues().put(c.getAttribute().getName(), c.serialize());
        }
        
        Gson gson = new Gson();
        String s = gson.toJson(model);

        System.out.println(s);
        
        kahve.put( getQueryKey( queryName ), s );
        
        if( defaultQuery ){
            kahve.put( getDefaultQueryKey(), queryName);
        }
        
        //Sorgu adı listeye ekleniyor. 
        List<String> ls = new ArrayList(getQueryNames());
        ls.add(queryName);
        
        String ss = Joiner.on(',').join(ls);
        kahve.put(getQueryNamesKey(), ss );
    }

    public void load( String name ){
        KahveEntry e = kahve.get( getQueryKey(name) );
        if( e == null ) {
            LOG.error("Saved Query not found : {}", getQueryKey(name) );
            return;
        }
        
        Gson gson = new Gson();
        QueryModel model = gson.fromJson(e.getAsString(), QueryModel.class);
        
        queryName = model.getName();
        queryDefinition.setResultLimit(model.getResultLimit());
        queryDefinition.setRowLimit(model.getRowLimit());
        
        queryDefinition.getColumns().clear();
        for( String s : model.getColumns() ){
            queryDefinition.getColumns().add( queryDefinition.findColumnByName(s));
        }
        
        queryDefinition.getFilters().clear();
        for( String s : model.getFilters() ){
            Filter f = queryDefinition.findFilterByName(s);
            queryDefinition.getFilters().add( f );
            String val = model.getFilterValues().get(s);
            if( val != null ){
                f.deserialize(model.getFilterValues().get(s));
            }
        }
    }
    
    /**
     * Saklanmış tüm sorguları siler.
     */
    public void deleteAll(){
        List<String> ls = getQueryNames();
        for( String s : ls ){
            kahve.remove( getQueryKey(s));
        }
        kahve.remove(getQueryNamesKey());
        kahve.remove(getDefaultQueryKey());
    }
    
    /**
     * saklanmış olan sorguların isim listesini döner.
     * @return 
     */
    public List<String> getQueryNames(){
        KahveEntry e = kahve.get(getQueryNamesKey());
        List<String> ls;
        if( e != null ){
            ls = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(e.getAsString());
        } else {
            ls = new ArrayList<>();
        }
        
        return ls;
    }
    
    /**
     * Verilen isimli sorguyu yükler
     * @param name 
     */
    public void loadQuery( String name ){
        queryName = name;
        load( name );
        search();
    }
    
    /**
     * Varsayılan sorguyu yükler yosak olduğu gibi bırakır.
     */
    public void loadDefaultQuery(){
        KahveEntry e = kahve.get(getDefaultQueryKey());
        if( e != null ){
            load(e.getAsString());
        } else {
            queryName = "Standart";
        }
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Boolean getDefaultQuery() {
        return defaultQuery;
    }

    public void setDefaultQuery(Boolean defaultQuery) {
        this.defaultQuery = defaultQuery;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    /**
     * Geriye ilgili browse için sorgu isimlerinin saklandığı kahve key'ini döndürür.
     * @return 
     */
    protected String getQueryNamesKey(){
        return "query.names." + getClass().getSimpleName();
    }
    
    /**
     * Geriye ilgili brose için sorgu key'ini döndürür.
     * @param s
     * @return 
     */
    protected String getQueryKey( String s ){
        return "query." + getClass().getSimpleName() + "." + s;
    }
    
    /**
     * Geriye ilgili brose için varsa default sorgunun saklandığı key'i döndürür.
     * @return 
     */
    protected String getDefaultQueryKey(){
        return "query.default." + getClass().getSimpleName();
    }
    
}
