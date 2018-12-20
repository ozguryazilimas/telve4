package com.ozguryazilim.telve.query.columns;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Saat tipi veriler için kolon oluşturur.
 * 
 * @author Hakan Uygun
 * @param <E>
 */
public class TimeColumn<E> extends Column<E>{

    public TimeColumn(SingularAttribute<? super E, ? extends Date> attribute, String labelKey) {
        super(attribute, labelKey);
    }

    @Override
    public String getTemplate() {
        return "timeColumn";
    }
    
}
