/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query;

import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Numerik tipte değer filtresi
 *
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <P> Veri tipi : Integer, Double, Foat, BidDecimal v.b.
 */
public class NumberFilter<E, P extends Number & Comparable> extends Filter<E, P> {

    public NumberFilter(SingularAttribute<? super E, P> attribute, String label) {
        super(attribute, label);

        setOperands(Operands.getNumberOperands());
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
    
}
