package com.ozguryazilim.telve.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enitity'ler üzerinde log ve benzeri yerlerde kullanmak üzere bizkey işaretlmeek için kullanılır.
 * 
 * @author Hakan Uygun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface BizKey {
    
}
