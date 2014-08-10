/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.impl.criteria.QueryCriteria;
import org.apache.deltaspike.data.impl.handler.AbstractDelegateQueryHandler;

/**
 * FilteredQuerySupport implementasyonu.
 * 
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 */
@Dependent
public class FiltredQuerySupporHandler<E> extends AbstractDelegateQueryHandler<E> implements FilteredQuerySupport<E>, Serializable{

    @Override
    public List<E> filteredQuery(List<Filter<E, ?>> filters) {
        QueryCriteria<E, E> criteria = new QueryCriteria<E, E>(getEntityClass(), getEntityClass(), getEntityManager());
        
        for( Filter<E, ?> f : filters ){
            f.decorateCriteria(criteria);
        }
        
        return criteria.getResultList();
    }

    @Override
    public Criteria<E, E> browseCriteria() {
        return new QueryCriteria<E, E>(getEntityClass(), getEntityClass(), getEntityManager());
    }

    
    
}
