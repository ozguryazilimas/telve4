/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.lookup.LookupControllerBase;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class EntityFilter<E extends EntityBase, T extends EntityBase> extends Filter<E, T, T>{

    
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
        } else if( getOperand() == FilterOperand.None ){
            criteria.isNull(getAttribute());
        }
    }

    @Override
    public void decorateCriteriaQuery(List<Predicate> predicates, CriteriaBuilder builder, Root<E> from) {
        if (getValue() != null) {
            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(from.get(getAttribute()), getValue()));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()), getValue()));
                    break;
                case None:
                    predicates.add(builder.isNull(from.get(getAttribute())));
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

    @Override
    public String serialize() {
        return Joiner.on("::").join(getOperand(), 
                getValue() == null ? "null" : getValue().getClass().getCanonicalName(), 
                getValue() == null ? "null" : getValue().getId());
    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        if (!"null".equals(ls.get(2))) {
            setValue((T)getLookupBean().findBy(Long.parseLong(ls.get(2))));
        } else {
            setValue(null);
        }
    }
    
}
