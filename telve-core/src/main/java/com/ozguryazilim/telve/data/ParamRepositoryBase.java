/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.data;

import com.ozguryazilim.telve.entities.ParamEntityBase;
import com.ozguryazilim.telve.entities.ParamEntityBase_;
import com.ozguryazilim.telve.entities.ViewModel;
import java.util.List;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Parametre tipi entityler için taban repository.
 *
 * @author Hakan Uygun
 * @param <E> Parametre tipi entity sınıfı
 */
public abstract class ParamRepositoryBase<E extends ParamEntityBase, R extends ViewModel> extends RepositoryBase<E, R> implements CriteriaSupport<E> {

    public static volatile SingularAttribute<ParamEntityBase, Long> id;

    /**
     * Geriye View Model sınıfı döndürülür.
     *
     * @return
     */
    protected abstract Class<R> getViewModelClass();

    protected abstract SingularAttribute<? super E, Long> getIdAttribute(); 
    
    protected abstract void buildProjection(Criteria<E, E> e);

    /**
     * Lookuplar tarafından kullanılan sorgu.
     *
     * Param modülleri için code ve ad üzerinden like ile arama yapar.
     *
     * @param searchText
     * @return
     */
    @Override
    public List<R> lookupQuery(String searchText) {
        //FIXME : Bu method bir şekilde çalışmadı :(
        return criteria()
                .or(
                        criteria().like(ParamEntityBase_.code, "%" + searchText + "%"),
                        criteria().like(ParamEntityBase_.name, "%" + searchText + "%")
                )
                .eq(ParamEntityBase_.active, true)
                .select(getViewModelClass(), 
                        attribute(getIdAttribute()),
                        attribute(ParamEntityBase_.code),
                        attribute(ParamEntityBase_.name))
                .getResultList();
                
    }

    public Criteria<E, E> lookupCriteria(String searchText) {
        return criteria()
                .or(
                        criteria().like(ParamEntityBase_.code, "%" + searchText + "%"),
                        criteria().like(ParamEntityBase_.name, "%" + searchText + "%")
                )
                .eq(ParamEntityBase_.active, true);
    }

    @Override
    public List<E> suggestion(String searchText) {
        return criteria()
                .or(
                        criteria().like(ParamEntityBase_.code, "%" + searchText + "%"),
                        criteria().like(ParamEntityBase_.name, "%" + searchText + "%")
                )
                .eq(ParamEntityBase_.active, true)
                .getResultList();
    }

    @Override
    public List<E> findAllActives() {
        return criteria()
                .eq(ParamEntityBase_.active, true)
                .getResultList();
    }
    
    
}
