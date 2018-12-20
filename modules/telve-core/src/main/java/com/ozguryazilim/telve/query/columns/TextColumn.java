package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Normal Text Out veren column tipi.
 * 
 * @author Hakan Uygun
 * @param <E> Entitity Sınıfı
 */
public class TextColumn<E> extends Column<E>{

    public TextColumn(SingularAttribute<? super E, ?> attribute, String labelKey) {
        super(attribute, labelKey);
    }

    
    @Override
    public String getTemplate() {
        return "column";
    }
    
}
