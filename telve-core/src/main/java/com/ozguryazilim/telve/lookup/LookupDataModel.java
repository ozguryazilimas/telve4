/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lookup Dialoğlarında kullanılacak olan data model sınıfı.
 * 
 * Lookup dialoğları bu sınıftan türetilmiş model sınıflara vermeliler.
 * 
 * @author Hakan Uygun
 * @param <T> Veri modeli olarak kullanılacak temel sınıf.
 */
public class LookupDataModel<T> extends ListDataModel<T> implements SelectableDataModel<T>{

    private static final Logger LOG = LoggerFactory.getLogger(LookupDataModel.class);
    
    private Map<String, String> columns = new HashMap<String, String>();
    
    
    /**
     * Verilen model için anahtar döndürür.
     * 
     * Şu anda List index bilgisi anahtar olarak döndürürlmektedir.
     * 
     * @param t
     * @return 
     */
    @Override
    public Object getRowKey(T t) {
        List<T> ls = (List<T>)getWrappedData();
        LOG.info("Object : #0, List : #1", t, ls );
        if( ls == null ) return null;
        return ls.indexOf(t);
    }

    /**
     * Verilen anahtar için model sınıfını döndürür.
     * 
     * Şu anda list index bilgisi anahtar olarak kullanılmaktadır.
     * 
     * @param key
     * @return 
     */
    @Override
    public T getRowData(String key) {
        List<T> ls = (List<T>)getWrappedData();
        LOG.info("Key : #0, List : #1", key, ls );
        if( ls == null ) return null;
        return ls.get(Integer.parseInt(key));
    }
    
    /**
     * Sunulacak olan kolon ve başlık bilgisi ekler.
     * 
     * @param name
     * @param caption 
     */
    public void addColumn( String name, String caption ){
       columns.put(name, caption);
    }

    /**
     * Kolon bilgisini döndürür.
     * 
     * @return 
     */
    public Map<String, String> getColumns() {
        return columns;
    }

    /**
     * Kolon bilgisini setler.
     * 
     * @param columns 
     */
    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }
    
    
    
}
