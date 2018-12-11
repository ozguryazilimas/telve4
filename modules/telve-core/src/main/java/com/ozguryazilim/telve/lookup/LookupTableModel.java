/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.entities.ViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tablo tipi Lookup Dialoğlarında kullanılacak olan data model sınıfı.
 * 
 * Lookup dialoğları bu sınıftan türetilmiş model sınıflara vermeliler.
 * 
 * @author Hakan Uygun
 * @param <T> Veri modeli olarak kullanılacak temel sınıf.
 */
public class LookupTableModel<T extends ViewModel> extends ListDataModel<T> implements LookupModel<T, T>, SelectableDataModel<T>{

    private static final Logger LOG = LoggerFactory.getLogger(LookupTableModel.class);

    private T selectedData;
    private T[] selectedDatas;
    private List<T> selectedAllDatas = new ArrayList<>();
    private Boolean multiSelect = false;
    private String profile;
    private String searchText;
    private String listener;
    private Map<String,String> profileProperties;
    
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
        LOG.info("Object : {}, List : {}", t, ls );
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
        LOG.info("Key : {}, List : {}", key, ls );
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

    @Override
    public T getSelectedData() {
        return selectedData;
    }

    @Override
    public void setSelectedData(T selectedData) {
        this.selectedData = selectedData;
    }

    @Override
    public T[] getSelectedDatas() {
        return selectedDatas;
    }

    @Override
    public void setSelectedDatas(T[] selectedDatas) {
        this.selectedDatas = selectedDatas;
        
        for( T t : selectedDatas ){
            if( !this.selectedAllDatas.contains(t)){
                this.selectedAllDatas.add(t);
            }
        }
    }

    public List<T> getSelectedAllDatas() {
        return selectedAllDatas;
    }

    public void setSelectedAllDatas(List<T> selectedAllDatas) {
        this.selectedAllDatas = selectedAllDatas;
    }

    @Override
    public Boolean getMultiSelect() {
        return multiSelect;
    }

    @Override
    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    @Override
    public String getProfile() {
        return profile;
    }

    @Override
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String getSearchText() {
        return searchText;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String getListener() {
        return listener;
    }

    @Override
    public void setListener(String listener) {
        this.listener = listener;
    }

    @Override
    public T getSelectedViewModel() {
        return getSelectedData();    
    }

    @Override
    public void setSelectedViewModel(T data) {
        setSelectedData(data);
    }

    @Override
    public List<T> getSelectedViewModels() {
        return  getSelectedAllDatas();
    }

    @Override
    public void setSelectedViewModels(List<T> data) {
        setSelectedDatas(data.toArray(selectedDatas));
    }

    @Override
    public void setData(List<T> dataList) {
        setWrappedData(dataList);
    }

    @Override
    public boolean isDataEmpty() {
        return getWrappedData() == null || ((List<T>)getWrappedData()).isEmpty();
    }

    @Override
    public void clearData() {
        if( getWrappedData() != null  ){
            ((List<T>)getWrappedData()).clear();
        }
    }

    @Override
    public String getModelType() {
        return "Table";
    }

    @Override
    public Object dataModel() {
        return this;
    }

    @Override
    public Boolean getLeafSelect() {
        return false; //Tablo için bir anlamı yok
    }

    @Override
    public void setLeafSelect(Boolean leafSelect) {
        //Yapılacak bişi yok. tablo için leaf select anlamlı bir bilgi değil.
    }

    @Override
    public Map<String, String> getProfileProperties() {
        return profileProperties;
    }

    @Override
    public void setProfileProperties(Map<String, String> profileProperties) {
        this.profileProperties = profileProperties;
    }

    @Override
    public void clearSelectedAllDatas() {
        this.selectedAllDatas.clear();
    }

    @Override
    public void clearSelections() {
        selectedData = null;
        selectedDatas = null;
        clearSelectedAllDatas();
    }

    @Override
    public Boolean getFullPathResult() {
        return false;
    }

    @Override
    public void setFullPathResult(Boolean fullPathResult) {
        //Sadece ağaç için anlamlı bir bilgi
    }

    
    
}
