/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.query.Operands;
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
    public String getTemplate() {
        return "stringFilter";
    }

}
