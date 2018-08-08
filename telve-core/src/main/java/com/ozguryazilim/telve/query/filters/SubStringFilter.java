/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Bir alt sınıfın bir alanında string sorgulama yapmak için.
 * 
 * Örneğin Fiş->Cari şeklinde bir ilişkide cari ismi ile arama için
 * 
 * @author oyas
 */
public class SubStringFilter<E,S> extends Filter<E, S, String> {

    private SingularAttribute< ? super S, String> subattribute;
    
    public SubStringFilter(SingularAttribute< ? super E, S> attribute, SingularAttribute< ? super S, String> subattribute, String label) {
        super(attribute,(SingularAttribute< ? super E, S>) subattribute, label);
        
        this.subattribute = subattribute;
        
        setOperands(Operands.getStringOperands());
        setOperand(FilterOperand.Contains);
    }
    
    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decorateCriteriaQuery(List<Predicate> predicates, CriteriaBuilder builder, Root<E> from) {
        if (!Strings.isNullOrEmpty(getValue())) {

            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal( from.get(getAttribute()).get(subattribute), getValue()));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()).get(subattribute), getValue()));
                    break;
                case Contains:
                    predicates.add(builder.like(from.get(getAttribute()).get(subattribute), "%" + getValue() + "%"));
                    break;
                case BeginsWith:
                    predicates.add(builder.like(from.get(getAttribute()).get(subattribute), getValue() + "%"));
                    break;
                case EndsWith:
                    predicates.add(builder.like(from.get(getAttribute()).get(subattribute), "%" + getValue() ));
                    break;
                case NotContains:
                    predicates.add(builder.notLike(from.get(getAttribute()).get(subattribute), "%" + getValue() + "%"));
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
