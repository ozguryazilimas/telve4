package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Boolean alanlar için filtre
 *
 * @author Hakan Uygun
 */
public class BooleanFilter<E> extends Filter<E, Boolean, Boolean> {

    private String keyPrefix;

    public BooleanFilter(SingularAttribute<? super E, Boolean> attribute, String label, String itemLabel) {
        super(attribute, label);

        keyPrefix = itemLabel;
        setOperands(Operands.getBooleanOperands());
        setOperand(FilterOperand.All);
    }

    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        switch (getOperand()) {
            case True:
                criteria.eq(getAttribute(), Boolean.TRUE);
                break;
            case False:
                criteria.notEq(getAttribute(), Boolean.FALSE);
                break;
            default:
                break;
        }
    }

    @Override
    public void decorateCriteriaQuery(List<Predicate> predicates, CriteriaBuilder builder, Root<E> from) {
        switch (getOperand()) {
            case True:
                predicates.add(builder.isTrue(from.get(getAttribute())));
                break;
            case False:
                predicates.add(builder.isFalse(from.get(getAttribute())));
                break;
            default:
                break;
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
        return getOperand().toString();
    }

    @Override
    public void deserialize(String s) {
        setOperand(FilterOperand.valueOf(s));
    }

}
