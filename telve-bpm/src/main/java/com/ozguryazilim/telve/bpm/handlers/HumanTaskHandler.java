/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

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
     * @return 
     */
    String taskName();
    
    /**
     * FA icon setinden bir icon ismi
     * @return 
     */
    String icon() default "fa-gears";
    
}
