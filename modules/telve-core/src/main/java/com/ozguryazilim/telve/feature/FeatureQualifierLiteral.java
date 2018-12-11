/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author oyas
 */
public class FeatureQualifierLiteral extends AnnotationLiteral<FeatureQualifier> implements FeatureQualifier{
    
    private final Class<? extends FeatureHandler> feature;

    public FeatureQualifierLiteral(Class<? extends FeatureHandler> feature) {
        this.feature = feature;
    }

    @Override
    public Class<? extends FeatureHandler> feauture() {
        return feature;
    }


    
}
