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

    @Override
    public String serialize() {
        return Joiner.on("::")
                .join(getOperand(), 
                        getValue() == null ? "null" : getValue().getClass().getCanonicalName(), 
                        getValue() == null ? "null" : getValue());
    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        //FIXME: Number bir değeri nasıl deserialize edeceğiz?
        //Sınıf ismini nasıl kullanırız? Tip kontrolünü nasıl geçeriz?
        //setValue(((P)Long.parseLong(ls.get(2))));
    }
    
}
