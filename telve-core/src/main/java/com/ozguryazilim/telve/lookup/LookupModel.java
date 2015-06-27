/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.entities.ViewModel;
import java.util.List;
import java.util.Map;

/**
 * Lookup Controllerlar için Model Sınıf API'si
 * 
 * @author Hakan Uygun
 * @param <R> Veri model sınıfı
 * @param <S> Selection model sınıfı
 */
public interface LookupModel<R extends ViewModel, S> {
    
    Boolean getMultiSelect();
    void setMultiSelect(Boolean multiSelect);
    
    Boolean getLeafSelect();
    void setLeafSelect(Boolean leafSelect);
    
    String getProfile();
    void setProfile( String profile );
    
    Map<String,String> getProfileProperties();
    void setProfileProperties( Map<String,String> prop );
    
    String getSearchText();
    void setSearchText( String searchText);
    
    String getListener();
    void setListener( String listener);
    
    S getSelectedData();
    void setSelectedData( S data );
    
    S[] getSelectedDatas();
    void setSelectedDatas( S[] data );
    
    R getSelectedViewModel();
    void setSelectedViewModel( R data );
    
    List<R> getSelectedViewModels();
    void setSelectedViewModels( List<R> data );
    
    /**
     * Sorgu sonu gelen verleri GUI modeline çevirir.
     * @param dataList 
     */
    void setData( List<R> dataList);
    
    /**
     * Veri listesinin boş olup olmadığını döndürür.
     * @return 
     */
    boolean isDataEmpty();  
    
    /**
     * Model üzerindeki veriyi siler;
     */
    void clearData();
    
    /**
     * UI katmanında hangi bileşenlerin kullanılacağına karar vermek için string değer.
     * Örneğin : Table, Tree v.b. gibi bir şey döner.
     * @return 
     */
    String getModelType();
    
    /**
     * UI tarafında kullanılacak bilgi listesi modelini döndürür.
     * Tablo için bir list iken Tree için bir Node olabilir.
     * @return 
     */
    Object dataModel();
    
    /**
     * UI tarafından seçilmiş listeyi silmek için kullanılır.
     */
    void clearSelectedAllDatas();
    
    /**
     * Seçilmiş olan verileri siler.
     */
    void clearSelections();
}
