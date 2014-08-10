/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.suggestion;

import com.ozguryazilim.telve.entities.SuggestionItem;
import com.ozguryazilim.telve.query.FilteredQuerySupport;
import java.util.List;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Suggestion Item'lar için DeltaSpike Repository.
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class SuggestionRepository extends AbstractEntityRepository<SuggestionItem, Long> implements CriteriaSupport<SuggestionItem>, FilteredQuerySupport<SuggestionItem>{
    
    
    /**
     * Verilen Group değerine sahip değerlerin listesini döndürür.
     * @param group
     * @return 
     */
    public abstract List<SuggestionItem> findByGroup(String group);
    
    /**
     * Verilen grup ve key'e sahip değerlerin listesini döndürür.
     * @param group
     * @param key
     * @return 
     */
    public abstract List<SuggestionItem> findByGroupAndKey(String group, String key);
    
}
