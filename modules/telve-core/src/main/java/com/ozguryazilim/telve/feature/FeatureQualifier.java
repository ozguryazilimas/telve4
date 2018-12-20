package com.ozguryazilim.telve.feature;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author oyas
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface FeatureQualifier {
    
    /**
     * Feature Sınıfı. Bütün Feature sınıfları @Feature ile işaretlenmiş, ve FeatureHandler'ı implemente eden sınıflardan oluşur.
     * @return 
     */
    Class<? extends FeatureHandler> feauture();
}
