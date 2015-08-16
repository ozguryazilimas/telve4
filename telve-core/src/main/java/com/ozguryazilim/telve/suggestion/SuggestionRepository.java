/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.suggestion;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.SuggestionItem;
import com.ozguryazilim.telve.entities.SuggestionItem_;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Suggestion Item'lar için DeltaSpike Repository.
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class SuggestionRepository extends RepositoryBase<SuggestionItem, SuggestionItem> implements CriteriaSupport<SuggestionItem>{
    
    
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
    
    @Override
    public List<SuggestionItem> browseQuery(List<Filter<SuggestionItem, ?>> filters) {
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<SuggestionItem> criteriaQuery = criteriaBuilder.createQuery(SuggestionItem.class);

        //From Tabii ki         
        Root<SuggestionItem> from = criteriaQuery.from(SuggestionItem.class);
        
        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();
        
        decorateFilters(filters, predicates, criteriaBuilder, from);
        
        //Person filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
        
        //İsme göre sıralayalım
        criteriaQuery.orderBy(criteriaBuilder.asc(from.get(SuggestionItem_.group)));
        
        //Haydi bakalım sonuçları alalım
        TypedQuery<SuggestionItem> typedQuery = entityManager().createQuery(criteriaQuery);
        List<SuggestionItem> resultList = typedQuery.getResultList();

        return resultList;
    }
    
}
