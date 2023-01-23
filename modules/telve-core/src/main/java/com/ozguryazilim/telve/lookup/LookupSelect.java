package com.ozguryazilim.telve.lookup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * Lookup Select dönüş qualiferı.
 * 
 * String değer ile fazladan filtre konabilir.
 * 
 * @author Hakan Uygun
 */
@Qualifier
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RUNTIME)
public @interface LookupSelect {

    // #70421
    /**
     * Observer için gerekirse ek filtre olması için
     * @return 
     */
    String value() default "";

    String test();
}
