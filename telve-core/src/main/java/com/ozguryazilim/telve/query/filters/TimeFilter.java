/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.query.Operands;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Saat filtresi.
 * 
 * TODO: Tıpkı Date filter'da olduğu gibi time içinde bazı makrolar olabilir. Son 1 saat içinde v.b.
 * 
 * @author Hakan Uygun
 * @param <E> Filtrenin uygulanacağı Entity Sınıfı
 */
public class TimeFilter<E> extends Filter<E, Date> {

    public TimeFilter(SingularAttribute<? super E, Date> attribute, String label) {
        super(attribute, label);
        
        //IN Yok!
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
                    criteria.gtOrEq(getAttribute(), getValue());
                    break;
                case Lesser:
                    criteria.lt(getAttribute(), getValue());
                    break;
                case LesserOrEqual:
                    criteria.ltOrEq(getAttribute(), getValue());
                    break;
                case In:
                    criteria.between(getAttribute(), getValue(), getValue2());
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
        return "timeFilter";
    }
    
}
