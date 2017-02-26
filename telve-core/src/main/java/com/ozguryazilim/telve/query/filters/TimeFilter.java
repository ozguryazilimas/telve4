/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.query.Operands;
import com.ozguryazilim.telve.utils.DateUtils;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.joda.time.DateTime;

/**
 * Saat filtresi.
 *
 * TODO: Tıpkı Date filter'da olduğu gibi time içinde bazı makrolar olabilir.
 * Son 1 saat içinde v.b.
 *
 * @author Hakan Uygun
 * @param <E> Filtrenin uygulanacağı Entity Sınıfı
 */
public class TimeFilter<E> extends Filter<E, Date, Date> {

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
                case In:
                    predicates.add(builder.between(from.get(getAttribute()), getValue(), getValue2()));
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
        return "timeFilter";
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
            DateTime parseDateTime = DateUtils.getDateTimeFormatter().parseDateTime(ls.get(1));
            setValue(parseDateTime.toDate());
        } else {
            setValue(null);
        }
    }

}
