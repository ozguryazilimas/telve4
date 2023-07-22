package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.config.LocaleSelector;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import java.util.Locale;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * StrinFilter sınıfı
 *
 * @author Hakan Uygun
 * @param <E> Entity Sınıf
 */
public class StringFilter<E> extends Filter<E, String, String> {

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
                    criteria.eqIgnoreCase(getAttribute(), getValue());
                    break;
                case NotEqual:
                    criteria.notEqIgnoreCase(getAttribute(), getValue());
                    break;
                case Contains:
                    criteria.likeIgnoreCase(getAttribute(), "%" + getValue() + "%");
                    break;
                case BeginsWith:
                    criteria.likeIgnoreCase(getAttribute(), getValue() + "%");
                    break;
                case EndsWith:
                    criteria.likeIgnoreCase(getAttribute(), "%" + getValue());
                    break;
                case NotContains:
                    criteria.notLikeIgnoreCase(getAttribute(), "%" + getValue() + "%");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void decorateCriteriaQuery( List<Predicate> predicates, CriteriaBuilder builder, Root<E> from ){
        if (getValue() != null && !getValue().isEmpty() ) {
            Locale sessionLocale = LocaleSelector.instance().getLocale();

            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(builder.lower(from.get(getAttribute())), getValue().toLowerCase(sessionLocale)));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(builder.lower(from.get(getAttribute())), getValue().toLowerCase(sessionLocale)));
                    break;
                case Contains:
                    predicates.add(builder.like(builder.lower(from.get(getAttribute())), "%" + getValue().toLowerCase(sessionLocale) + "%"));
                    break;
                case BeginsWith:
                    predicates.add(builder.like(builder.lower(from.get(getAttribute())), getValue().toLowerCase(sessionLocale) + "%"));
                    break;
                case EndsWith:
                    predicates.add(builder.like(builder.lower(from.get(getAttribute())), "%" + getValue().toLowerCase(sessionLocale)));
                    break;
                case NotContains:
                    predicates.add(builder.notLike(builder.lower(from.get(getAttribute())), "%" + getValue().toLowerCase(sessionLocale) + "%"));
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
