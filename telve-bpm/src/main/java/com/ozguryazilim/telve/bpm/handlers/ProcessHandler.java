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
 * Process Handler işaretçisi
 * @author Hakan Uygun
 */
@Stereotype
@SessionScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface ProcessHandler {
    
    /**
     * Process ID
     * @return 
     */
    String processId();
    
    /**
     * Kullanılacak olan BPMN kaynak dosya ismi
     * @return 
     */
    String bpmn();
    
    /**
     * UI için süreç ikonu
     * @return 
     */
    String icon() default ""; 
    
    /**
     * UI için start dialoğu var mı?
     * @return 
     */
    boolean hasStartDialog() default false;
}
