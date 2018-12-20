package com.ozguryazilim.telve.contact;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * ContactResolver tarafından kullanılacak olan veri kaynaklarını işaretler.
 * @author Hakan Uygun
 */
@Stereotype
@Named
@ApplicationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ContactSource {
    /**
     * Sorgu tipi için kullanılacak isim.
     * 
     * Eğer verilmezse sınıf ismini kullanır.
     * 
     * @return 
     */
    String name() default "";
}
