package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Link üretecek column tipi
 * @author Hakan Uygun
 * @param <E>
 */
public class LinkColumn<E> extends Column<E>{
    
    public LinkColumn( SingularAttribute<? super E, ?> attribute, String labelKey ){
        super(attribute, labelKey);
    }

    @Override
    public String getTemplate() {
        return "linkcolumn";
    }
    
}
