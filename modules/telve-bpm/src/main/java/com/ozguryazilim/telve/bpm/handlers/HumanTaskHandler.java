package com.ozguryazilim.telve.bpm.handlers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Named;

/**
 * BPM HumanTaskHandler işaretçisi
 * @author Hakan Uygun
 */
@Stereotype
@SessionScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface HumanTaskHandler {
    
    /**
     * İlişkilendirildiği Human Task'ın ismi
     * 
     * camunda için bu değer formkey'e denk gelir.
     * 
     * @return 
     */
    String taskName();
    
    /**
     * FA icon setinden bir icon ismi
     * @return 
     */
    String icon() default "fa-gears";
    
    /**
     * Task actionlarda yorum girişi zorunlu mu?
     * @return 
     */
    boolean commentRequired() default false;
}
