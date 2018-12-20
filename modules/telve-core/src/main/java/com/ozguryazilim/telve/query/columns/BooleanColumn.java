package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Boolean veri tipleri için kolon oluşturur.
 * 
 * @author Hakan Uygun
 * @param <E> işlenecek olan Entity sınıfı.
 */
public class BooleanColumn<E> extends Column<E>{

    public BooleanColumn(SingularAttribute<? super E, ? extends Boolean> attribute, String labelKey, String keyPrefix) {
        super(attribute, labelKey);
        setKeyPrefix(keyPrefix);
    }

    @Override
    public String getTemplate() {
        return "booleanColumn";
    }
    
}
