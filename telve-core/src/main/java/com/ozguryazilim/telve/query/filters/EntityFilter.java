/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.lookup.LookupControllerBase;
import com.ozguryazilim.telve.query.Operands;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Entity tipindeki sınıfları filtreler.
 * 
 * @author Hakan Uygun
 * @param <E> Üzerinde sorgu yapılacak Entity sınıfı
 * @param <T> Sorgu için kullanılacak olan değer Entity Sınıfı
 */
public class EntityFilter<E extends EntityBase, T extends EntityBase> extends Filter<E, T>{

    
    private final Class<? extends LookupControllerBase<T,?>> lookupClazz;
    
    public EntityFilter(SingularAttribute<? super E, T> attribute, Class< ? extends LookupControllerBase<T,?>> lookupClazz, String label ) {
        super(attribute, label);
        
        this.lookupClazz = lookupClazz;
        
        setOperands(Operands.getEntityOperands());
        setOperand(FilterOperand.All);
    }

    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        if (getValue() != null) {
            switch (getOperand()) {
                case Equal:
                    criteria.eq(getAttribute(), getValue());
                    break;
                case NotEqual:
                    criteria.notEq(getAttribute(), getValue());
                    break;
                case None:
                    criteria.isNull(getAttribute());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getTemplate() {
        return "entityFilter";
    }
    
    /**
     * Geriye Lookup CDI Bean instance'ını döndürür.
     * @return 
     */
    public LookupControllerBase getLookupBean(){
        return BeanProvider.getContextualReference(lookupClazz, true);
    }
    
}
