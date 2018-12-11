/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 *
 * @author oyas
 */
public class UserFilter<E> extends Filter<E, String, String> {

    
    public UserFilter(SingularAttribute<? super E, String> attribute, String label) {
        super(attribute, label);

        setOperands(Operands.getEnumOperands());
        setOperand(FilterOperand.All);
        setValue("");
    }

    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decorateCriteriaQuery(List<Predicate> predicates, CriteriaBuilder builder, Root<E> from) {
        if (!Strings.isNullOrEmpty(getValue())) {

            String val = getValue();
            
            if( "ME".equals(getValue()) ){
                Identity identity = BeanProvider.getContextualReference(Identity.class);
                val = identity.getLoginName();
            }
            
            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(from.get(getAttribute()), val));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()), val));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getTemplate() {
        return "userFilter";
    }

    @Override
    public String serialize() {
        return Joiner.on("::").join(getOperand(), getValue() == null ? "null" : getValue());
    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        if (!"null".equals(ls.get(1))) {
            setValue(ls.get(1));
        } else {
            setValue(null);
        }
    }

}
