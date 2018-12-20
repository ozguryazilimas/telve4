package com.ozguryazilim.telve.data;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.Column;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;
import org.apache.deltaspike.data.impl.criteria.QueryCriteria;

/**
 *
 * @author haky
 * @param <E> Üzerinde çalışılacak Entity
 * @param <R> Lookup, Browse v.b.'de kullanılacak olan View Model
 */
//@Dependent
public abstract class RepositoryBase<E extends EntityBase, R extends ViewModel> extends AbstractEntityRepository<E, Long> implements CriteriaSupport<E>{

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
     * Suggestbox'larda kullanılır.
     *
     * Varsayılan olarak boş liste döndürür. Override edilmesi gerekir.
     *
     * @param searchText
     * @return
     */
    public List<E> suggestionLeaf(String searchText) {
        return Collections.EMPTY_LIST;
    }

    /**
     * Geriye aktif kayıtları döndürür.
     * @return 
     */
    public List<E> findAllActives(){
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Browse'ların kullanımı için Criteria döndürür.
     *
     * Eğer varsayılan filtre eklenmek istenirse override edilmelidir.
     *
     * @return
     */
    public Criteria<E, R> browseCriteria() {
        return (Criteria<E, R>) new QueryCriteria<>(entityClass(), entityClass(), entityManager());
    }


    protected abstract Class<R> getResultClass();
    
    
    public List<R> browseQuery( QueryDefinition queryDefinition  ){
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Gelenfiltreler için Predicate'leri oluşturup verilen listeye ekler.
     * @param filters
     * @param predicates
     * @param builder
     * @param from 
     */
    protected void decorateFilters( List<Filter<E, ?,?>> filters, List<Predicate> predicates, CriteriaBuilder builder, Root<E> from ){
        
        for( Filter<E,?,?> f : filters ){
            f.decorateCriteriaQuery(predicates, builder, from);
        }
        
    }
    
    protected List<Order> decorateSorts( List<Column<? super E>> sorters, CriteriaBuilder builder, Root<E> from ){
        List<Order> result = new ArrayList<>();
        for( Column<? super E> c : sorters ){
            if( c.getSortAsc()){
                result.add( builder.asc(from.get((SingularAttribute<E, ?>)c.getAttribute())));
            } else {
                result.add( builder.desc(from.get((SingularAttribute<E, ?>)c.getAttribute())));
            }
        }
        
        return result;
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
    
    public abstract void deleteById( Long id );
    
    public Class<E> getEntityClass(){
        return entityClass();
    }
}
