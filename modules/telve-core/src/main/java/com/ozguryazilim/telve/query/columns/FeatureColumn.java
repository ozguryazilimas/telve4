/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import com.ozguryazilim.telve.entities.FeaturePointer;
import javax.persistence.metamodel.Attribute;

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
