/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query;

import com.ozguryazilim.telve.query.columns.Column;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.DualListModel;

/**
 * Browse içerisinde kullanılmak üzere sorgu tanımı ve toplanan veri modellerini
 * tutar.
 *
 *
 *
 * @author Hakan Uygun
 * @param <E> Sorgu yapılacka olan Entity
 * @param <R> Geri dönecek olan Model
 */
public class QueryDefinition<E, R> {

    /**
     * Genel bir sorgu alanı için veri saklanır.
     * Sorgu implementasyonu hangi alanları arayacağını belirler.
     * Genelde Ad Soyad, Açıklama v.b. gibi alanlar için kullanılır.
     * 
     * QueryModel üzerinde saklanmaz.
     */
    private String searchText;
    
    private List<Filter<E, ?, ?>> visibleFilters = new ArrayList<>();
    private List<Filter<E, ?, ?>> filters = new ArrayList<>();
    private List<Filter<E, ?, ?>> quickFilters = new ArrayList<>();
    private List<Filter<?, ?, ?>> extraFilters = new ArrayList<>();

    /**
     * Tüm kolonlar
     */
    private List<Column<? super E>> allColumns = new ArrayList<>();

    /**
     * Görünen kolonlar
     */
    private List<Column<? super E>> columns = new ArrayList<>();

    /**
     * Sıralama için kullanılabilecek kolonlar.
     */
    private List<Column<? super E>> sortables = new ArrayList<>();
    
    private List<Column<? super E>> sorters = new ArrayList<>();
    
    private Integer resultLimit = 100;
    private Integer rowLimit = 20;

    /**
     * Yeni filtre ekler.
     *
     * @param filter
     * @param visible
     * @param required
     * @return
     */
    public QueryDefinition<E, R> addFilter(Filter<E, ?, ?> filter, boolean visible ) {

        filters.add(filter);

        if (visible ) {
            visibleFilters.add(filter);
        }

        return this;
    }

    
    public QueryDefinition<E, R> addFilter(Filter<E, ?, ?> filter) {
        return addFilter(filter, true);
    }
    
    public QueryDefinition<E, R> addQuickFilter(Filter<E, ?, ?> filter) {
        quickFilters.add(filter);
        addFilter(filter,false);
        return this;
    }
    
    public QueryDefinition<E, R> addExtraFilter(Filter<?, ?, ?> filter) {
        extraFilters.add(filter);
        return this;
    }

    public QueryDefinition<E, R> addColumn(Column<? super E> column, boolean visible, boolean sortable) {
        allColumns.add(column);
        if (visible) {
            columns.add(column);
        }
        
        if( sortable ){
            sortables.add(column);
        }
        
        return this;
    }

    public QueryDefinition<E, R> addColumn(Column<? super E> column, boolean visible) {
        return addColumn(column, visible, true);
    }
    
    public QueryDefinition<E, R> addColumn(Column<? super E> column) {
        return addColumn(column, false, true);
    }

    public List<Filter<E, ?, ?>> getVisibleFilters() {
        return visibleFilters;
    }

    public List<Filter<E, ?, ?>> getFilters() {
        return filters;
    }

    public List<Filter<E, ?, ?>> getQuickFilters() {
        return quickFilters;
    }
    
    public List<Filter<?, ?, ?>> getExtraFilters() {
        return extraFilters;
    }
    
    public List<Column<? super E>> getAllColumns() {
        return allColumns;
    }

    public List<Column<? super E>> getColumns() {
        return columns;
    }

    public Integer getResultLimit() {
        return resultLimit;
    }

    public void setResultLimit(Integer resultLimit) {
        this.resultLimit = resultLimit;
    }

    public Integer getRowLimit() {
        return rowLimit;
    }

    public void setRowLimit(Integer rowLimit) {
        this.rowLimit = rowLimit;
    }

    public boolean isVisible(Filter filter) {
        return visibleFilters.contains(filter);
    }

    /**
     * PrimeFaces pickList bileşeni için model
     *
     * @return
     */
    public DualListModel<Column<? super E>> getPickListColumns() {
        //TODO: Burada bir performans problemi olabilir mi bir inceleyelim
        DualListModel<Column<? super E>> pickListColumns = new DualListModel<Column<? super E>>(getAvailColumns(), getColumns());
        return pickListColumns;
    }

    public void setPickListColumns(DualListModel<Column<? super E>> columns) {
         //Mutlaka 1 kolon seçilmeli yoksa önceki durumu hiç değiştirmeyelim
         if( !columns.getTarget().isEmpty() ){
             this.columns.clear();
             this.columns.addAll(columns.getTarget());
         }
    }

    public List<Column<? super E>> getAvailColumns() {
        List<Column<? super E>> ls = new ArrayList<>();

        for (Column col : allColumns) {
            //TODO: Burada hak kontrolüne de bakılmalı...
            if (!columns.contains(col)) {
                ls.add(col);
            }
        }

        return ls;
    }

    public void setAvailColumns(List<Column<? super E>> allColumns) {
    }

    /**
     * PrimeFaces pickList bileşeni için model
     *
     * @return
     */
    public DualListModel<Column<? super E>> getSortListColumns() {
        //TODO: Burada bir performans problemi olabilir mi bir inceleyelim
        DualListModel<Column<? super E>> pickListColumns = new DualListModel<>(getAvailSortables(), getSorters());
        return pickListColumns;
    }
    
    public void setSortListColumns(DualListModel<Column<? super E>> columns) {
        this.sorters.clear();
        this.sorters.addAll(columns.getTarget());
    }

    public List<Column<? super E>> getAvailSortables() {
        List<Column<? super E>> ls = new ArrayList<>();

        for (Column col : sortables) {
            //TODO: Burada hak kontrolüne de bakılmalı...
            if (!sorters.contains(col)) {
                ls.add(col);
            }
        }

        return ls;
    }

    public void setAvailSortables(List<Column<? super E>> allColumns) {
    }
    
    public List<Column<? super E>> getSortables() {
        return sortables;
    }

    public void setSortables(List<Column<? super E>> sortables) {
        this.sortables = sortables;
    }

    public List<Column<? super E>> getSorters() {
        return sorters;
    }

    public void setSorters(List<Column<? super E>> sorters) {
        this.sorters = sorters;
    }
    
    public void toggleSortOrder( String name ){
        Column<? super E> c = findColumnByName( name );
        c.setSortAsc(!c.getSortAsc());
    }
    
    /**
     * Verilen isimli kolon tanımını arar bulamazsa null döner.
     * @param name
     * @return 
     */
    public Column<? super E> findColumnByName( String name ){
        for( Column<? super E> c : allColumns ){
            if( c.getName().equals(name) ) return c;
        }
        return null;
    }

    /**
     * Verilen isimli filtre tanımını arar bulamazsa null döner.
     * @param name
     * @return 
     */
    public Filter<E, ?, ?> findFilterByName( String name ){
        for( Filter<E, ?, ?> c : filters ){
            if( c.getAttribute().getName().equals(name) ) return c;
        }
        return null;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
    
}
