/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Numerik tipte değer filtresi
 *
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <P> Veri tipi : Integer, Double, Foat, BidDecimal v.b.
 */
public abstract class NumberFilter<E, P extends Number & Comparable> extends Filter<E, P, P> {

    public NumberFilter(SingularAttribute<? super E, P> attribute, String label, P defaultValue) {
        super(attribute, label);

        setOperands(Operands.getNumberOperands());
        setOperand(FilterOperand.All);
        setValue(defaultValue);
        setValue2(defaultValue);
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
                case Greater:
                    criteria.gt(getAttribute(), getValue());
                    break;
                case GreaterOrEqual:
                    criteria.gtOrEq( getAttribute(), getValue() );
                    break;
                case Lesser:
                    criteria.lt(getAttribute(), getValue());
                    break;
                case LesserOrEqual:
                    criteria.ltOrEq(getAttribute(), getValue());
                    break;
                case Between:
                    criteria.between(getAttribute(), getValue(), getValue2());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void decorateCriteriaQuery( List<Predicate> predicates, CriteriaBuilder builder, Root<E> from ){
        if (getValue() != null) {
            
            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(from.get(getAttribute()), getValue()));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()), getValue()));
                    break;
                case Greater:
                    predicates.add(builder.greaterThan(from.get(getAttribute()), getValue()));
                    break;
                case GreaterOrEqual:
                    predicates.add(builder.greaterThanOrEqualTo(from.get(getAttribute()), getValue()));
                    break;
                case Lesser:
                    predicates.add(builder.lessThan(from.get(getAttribute()), getValue()));
                    break;
                case LesserOrEqual:
                    predicates.add(builder.lessThanOrEqualTo(from.get(getAttribute()), getValue()));
                    break;
                case Between:
                    predicates.add(builder.between(from.get(getAttribute()), getValue(), getValue2()));
                    break;
                default:
                    break;
            }
        }
    }
    
    @Override
    public String getTemplate() {
        return "numberFilter";
    }

    /**
     * Geriye GUI'den kaç ondalık hane alınacağını döndürür.
     * @return 
     */
    public int getDecimalPlaces(){
        //TODO: Acaba bir değer olarak kullanıcıdan mı alsak?
        if( getValue() instanceof Long || getValue() instanceof Integer ){
            return 0;
        } else {
            return 2;
        }
    }

    @Override
    public String serialize() {
        return Joiner.on("::")
                .join(getOperand(), 
                        getValue() == null ? "null" : getValue());
    }

    
    
}
