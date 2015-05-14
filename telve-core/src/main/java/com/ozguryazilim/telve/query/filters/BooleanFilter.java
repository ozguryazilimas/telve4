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
        setValue(Boolean.TRUE);
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
    public void decorateCriteriaQuery(List<Predicate> predicates, CriteriaBuilder builder, Root<E> from) {
        if (getValue() != null) {
            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(from.get(getAttribute()), getValue()));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()), getValue()));
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

    @Override
    public String serialize() {
        return Joiner.on("::").join(getOperand(), getValue() ? "T" : "F");
    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        
        setValue("T".equals( ls.get(1)));
    }
    
    
}
