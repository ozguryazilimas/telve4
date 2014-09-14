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

    private List<Filter<E, ?>> allFilters = new ArrayList<Filter<E, ?>>();
    private List<Filter<E, ?>> reqFilters = new ArrayList<Filter<E, ?>>();
    private List<Filter<E, ?>> filters = new ArrayList<Filter<E, ?>>();

    /**
     * Tüm kolonlar
     */
    private List<Column<E>> allColumns = new ArrayList<>();

    /**
     * Görünen kolonlar
     */
    private List<Column<E>> columns = new ArrayList<>();

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
    public QueryDefinition<E, R> addFilter(Filter<E, ?> filter, boolean visible, boolean required) {

        allFilters.add(filter);

        if (visible || required) {
            filters.add(filter);
        }

        if (required) {
            reqFilters.add(filter);
        }

        return this;
    }

    public QueryDefinition<E, R> addFilter(Filter<E, ?> filter, boolean visible) {
        return addFilter(filter, visible, false);
    }

    public QueryDefinition<E, R> addFilter(Filter<E, ?> filter) {
        return addFilter(filter, false, false);
    }

    public QueryDefinition<E, R> addColumn(Column<E> column, boolean visible) {
        allColumns.add(column);
        if (visible) {
            columns.add(column);
        }
        return this;
    }

    public QueryDefinition<E, R> addColumn(Column<E> column) {
        return addColumn(column, false);
    }

    public List<Filter<E, ?>> getAllFilters() {
        return allFilters;
    }

    public List<Filter<E, ?>> getReqFilters() {
        return reqFilters;
    }

    public List<Filter<E, ?>> getFilters() {
        return filters;
    }

    public List<Column<E>> getAllColumns() {
        return allColumns;
    }

    public List<Column<E>> getColumns() {
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
        return filters.contains(filter);
    }

    public boolean isRequired(Filter filter) {
        return reqFilters.contains(filter);
    }

    public void toggleFilter(Filter filter) {

        if (!isRequired(filter)) {
            if (filters.contains(filter)) {
                filters.remove(filter);
            } else {
                filters.add(filter);
            }
        }
    }

    /**
     * PrimeFaces pickList bileşeni için model
     *
     * @return
     */
    public DualListModel<Column<E>> getPickListColumns() {
        //TODO: Burada bir performans problemi olabilir mi bir inceleyelim
        DualListModel<Column<E>> pickListColumns = new DualListModel<Column<E>>(getAvailColumns(), getColumns());
        return pickListColumns;
    }

    public void setPickListColumns(DualListModel<Column> columns) {
        /* FIXME: Burası ne olsa da olsa
         this.pickListColumns = columns;
         //Mutlaka 1 kolon seçilmeli yoksa önceki durumu hiç değiştirmeyelim
         if( !columns.getTarget().isEmpty() ){
         this.columns = columns.getTarget();
         }
         */
    }

    public List<Column<E>> getAvailColumns() {
        List<Column<E>> ls = new ArrayList<Column<E>>();

        for (Column col : allColumns) {
            //TODO: Burada hak kontrolüne de bakılmalı...
            if (!columns.contains(col)) {
                ls.add(col);
            }
        }

        return ls;
    }

    public void setAvailColumns(List<Column<E>> allColumns) {
    }
    
    
}
