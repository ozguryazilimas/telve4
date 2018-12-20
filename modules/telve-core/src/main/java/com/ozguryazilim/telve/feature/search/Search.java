package com.ozguryazilim.telve.feature.search;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Feature'lara Search handler tanımlar.
 * 
 * @author Hakan Uygun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Search {
    
    /**
     * Hangi handlerın kullanılacağı
     * 
     * @return 
     */
    Class<? extends AbstractFeatureSearchHandler> handler() ;
}
