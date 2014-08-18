/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.WindowScoped;

/**
 * OptionPane kontrol sınıfları işaretçisi.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@WindowScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface OptionPane {
   
    /**
     * Pane için kullanılacak view'in hangisi olduğu
     * 
     * @return 
     */
    Class<? extends ViewConfig> optionPage(); 
    
    /**
     * Eğer verilmezse sınıf adını kullanır.
     * @return 
     */
    String permission() default "";
}
