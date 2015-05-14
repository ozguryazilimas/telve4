/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * StrinFilter sınıfı
 *
 * @author Hakan Uygun
 * @param <E> Entity Sınıf
 */
public class StringFilter<E> extends Filter<E, String> {

    public StringFilter(SingularAttribute< ? super E, String> attribute, String label) {
        super(attribute, label);

        setOperands(Operands.getStringOperands());
        setOperand(FilterOperand.Contains);
    }

    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        if (getValue() != null && !getValue().isEmpty() ) {

            switch (getOperand()) {
                case Equal:
                    criteria.eq(getAttribute(), getValue());
                    break;
                case NotEqual:
                    criteria.notEq(getAttribute(), getValue());
                    break;
                case Contains:
                    criteria.like(getAttribute(), "%" + getValue() + "%");
                    break;
                case BeginsWith:
                    criteria.like(getAttribute(), getValue() + "%");
                    break;
                case EndsWith:
                    criteria.like(getAttribute(), "%" + getValue() );
                    break;
                case NotContains:
                    criteria.notLike(getAttribute(), "%" + getValue() + "%");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void decorateCriteriaQuery( List<Predicate> predicates, CriteriaBuilder builder, Root<E> from ){
        if (getValue() != null && !getValue().isEmpty() ) {

            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(from.get(getAttribute()), getValue()));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()), getValue()));
                    break;
                case Contains:
                    predicates.add(builder.like(from.get(getAttribute()), "%" + getValue() + "%"));
                    break;
                case BeginsWith:
                    predicates.add(builder.like(from.get(getAttribute()), getValue() + "%"));
                    break;
                case EndsWith:
                    predicates.add(builder.like(from.get(getAttribute()), "%" + getValue() ));
                    break;
                case NotContains:
                    predicates.add(builder.notLike(from.get(getAttribute()), "%" + getValue() + "%"));
                    break;
                default:
                    break;
            }
        }
    }
   
    
    @Override
    public String getTemplate() {
        return "stringFilter";
    }

    @Override
    public String serialize() {
        return Joiner.on("::").join(getOperand(), getValue() == null ? "null" : getValue());
    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        if( !"null".equals(ls.get(1))){
            setValue(ls.get(1));
        } else {
            setValue(null);
        }
    }

}
