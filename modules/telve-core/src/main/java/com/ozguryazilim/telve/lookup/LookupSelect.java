/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    
    /**
     * Observer için gerekirse ek filtre olması için
     * @return 
     */
    String value() default "";
}
