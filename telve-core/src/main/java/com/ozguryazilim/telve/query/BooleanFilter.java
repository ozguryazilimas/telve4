/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Boolean alanlar için filtre
 * @author Hakan Uygun
 */
public class BooleanFilter<E> extends Filter<E, Boolean>{

    private String keyPrefix;
    
    public BooleanFilter(SingularAttribute<? super E, Boolean> attribute, String label, String itemLabel) {
        super(attribute, label);
        
        keyPrefix = itemLabel;
        setOperands(Operands.getEnumOperands());
        setOperand(FilterOperand.Equal);
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
                default:
                    break;
            }
        }
    }

    @Override
    public String getTemplate() {
        return "booleanFilter";
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
    
    
}
