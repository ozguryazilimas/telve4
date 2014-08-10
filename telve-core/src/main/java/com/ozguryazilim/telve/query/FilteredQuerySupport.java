/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import java.util.List;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * DeltaSpike Repository Extention.
 * 
 * Bu interface sayesinde Repository'ler GUI üzerinden filtrelenir hale gelecekler.
 * 
 * @author Hakan Uygun
 * @param <E> Entity Sınıfı
 */
public interface FilteredQuerySupport<E> {
    
    /**
     * Verilen Filtreleri kullanarak sonuç Döndürür.
     * @return 
     */
    List<E> filteredQuery( List<Filter<E, ?>> filters );
    
    
    Criteria<E,E> browseCriteria();
}
