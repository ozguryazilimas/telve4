/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature.search;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Generic Feature arama için alt yapı.
 * 
 * Generic arama destekleyen her feature bu sınıftan bir adet implemente ederek Feature'tanımına ekler.
 * 
 * Bu sayede GlobalSearch ya da FeatureLookup yapıları çalışabilir.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractFeatureSearchHandler implements Serializable{
    
    /**
     * Geriye yapılan arama sonuçlarını döndürür.
     * 
     * @param filter
     * @return 
     */
    public abstract List<FeatureSearchResult> search( String searchText, Map<String,Object> params );
    
}
