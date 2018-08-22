package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Splitter;
import java.util.List;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * @author yusuf
 * @param <E>
 * @param <P>
 */
public class RatingFilter<E> extends NumberFilter<E, Integer> {

    private Integer length = 5;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public RatingFilter(SingularAttribute<? super E, Integer> attribute, String label, Integer defaultValue, Integer starsLength) {
        super(attribute, label, defaultValue);
        length = starsLength;

    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        setValue(new Integer(ls.get(1)));
    }

    @Override
    public String getTemplate() {
        return "ratingFilter";
    }
}
