/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.data;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.impl.criteria.QueryCriteria;

/**
 *
 * @author haky
 * @param <E> Üzerinde çalışılacak Entity
 * @param <R> Lookup, Browse v.b.'de kullanılacak olan View Model
 */
//@Dependent
public abstract class RepositoryBase<E extends EntityBase, R extends ViewModel> extends AbstractEntityRepository<E, Long> {

    /**
     * Yeni entity oluşturur.
     *
     * Eğer varsayılan değerleri değiştirmek istenirse override edilebilir.
     *
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public E createNew() throws InstantiationException, IllegalAccessException {
        return entityClass().newInstance();
    }

    /**
     * Lookuplar tarafından kullanılan sorgu.
     *
     * Varsayılan olarak boş liste döndürür. Override edilmesi gerekir.
     *
     * @param searchText
     * @return
     */
    public List<R> lookupQuery(String searchText) {
        return Collections.EMPTY_LIST;
    }

    /**
     * Suggestbox'larda kullanılır.
     *
     * Varsayılan olarak boş liste döndürür. Override edilmesi gerekir.
     *
     * @param searchText
     * @return
     */
    public List<E> suggestion(String searchText) {
        return Collections.EMPTY_LIST;
    }

    /**
     * Browse'ların kullanımı için Criteria döndürür.
     *
     * Eğer varsayılan filtre eklenmek istenirse override edilmelidir.
     *
     * @return
     */
    public Criteria<E, E> browseCriteria() {
        return new QueryCriteria<>(entityClass(), entityClass(), entityManager());
    }


    /**
     * Entity siler.
     * Orjinal olan deattach hatası veriyordu.
     * @param entity 
     */
    @Override
    public void remove(E entity) {
        
        EntityManager em = entityManager();
        entity = em.merge(entity);
        em.remove(entity);
        em.flush();
    }
}
