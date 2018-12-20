package com.ozguryazilim.telve.dashboard;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * Dashlet sınıflarını işretlemek için kullanılır.
 * @author Hakan Uygun
 */
@Stereotype
@SessionScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface Dashlet {
    
    /**
     * Dashlet'in yetki tanımı.
     * 
     * Varsayılan sınıf adıdır.
     * 
     * @return 
     */
    String permission() default "";
    
    /**
     * Dashlet'in sahip olduğu yetenekleri belirler.
     * 
     * Varsayılan olarak sadece sunulabilir.
     * 
     * @return 
     */
    DashletCapability[] capability() default {};
}
