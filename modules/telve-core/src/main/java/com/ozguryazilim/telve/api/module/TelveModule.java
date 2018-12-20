package com.ozguryazilim.telve.api.module;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;

/**
 * Telve Modul tanımlama annotation'ı
 * @author Hakan Uygun
 */
@Stereotype
@ApplicationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface TelveModule {
    /**
     * Eğer verilmezse sınıf adını alır.
     * @return 
     */
    String name() default "";
    
    /**
     * perm.xml dosyasının adı.
     * 
     * Eğer verilmezde moduladı.perm.xml arayacaktır.
     * @return 
     */
    String permissions() default "";
    
    /**
     * Messages dosyasının adı.
     * 
     * Eğer verilmezde moduladı-messages.properties arayacaktır.
     * @return 
     */
    String messages() default "";
}
