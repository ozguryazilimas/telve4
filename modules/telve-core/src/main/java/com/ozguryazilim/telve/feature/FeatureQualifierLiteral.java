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
