package com.ozguryazilim.telve.query.columns;

import com.ozguryazilim.telve.entities.FeaturePointer;
import jakarta.persistence.metamodel.Attribute;

/**
 *
 * @author oyas
 */
public class FeatureColumn<E> extends Column<E> {

    public FeatureColumn(Attribute<? super E, FeaturePointer> attribute, String labelKey) {
        super(attribute, labelKey);
    }

    @Override
    public String getTemplate() {
        return "featureColumn";
    }
    
}
