package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * @author yusuf
 */
public class SubLinkColumn<E, F> extends Column<E> {

    private SingularAttribute<? super F, ?> subattribute;

    public SubLinkColumn(SingularAttribute<? super E, ?> attribute, SingularAttribute<? super F, ?> subattribute, String labelKey) {
        super(attribute, labelKey);
        this.subattribute = subattribute;
    }

    @Override
    public String getTemplate() {
        return "subLinkColumn";
    }

    public SingularAttribute<? super F, ?> getSubattribute() {
        return subattribute;
    }

    public void setSubattribute(SingularAttribute<? super F, ?> subattribute) {
        this.subattribute = subattribute;
    }

    public String getSubName() {
        return getSubattribute().getName();
    }

}
