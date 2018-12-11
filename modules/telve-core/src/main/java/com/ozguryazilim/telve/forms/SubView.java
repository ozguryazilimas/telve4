/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;

/**
 * SubView işaretleyici.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@GroupedConversationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface SubView {
   
    /**
     * MasterFormun kim olduğu
     * @return 
     */
    Class<? extends ViewConfig> containerPage();
    
    /**
     * SubView'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> viewPage();
    
    /**
     * Yetki kontrol domaini
     * @return 
     */
    String permission();
    
    /**
     * Subview'ın içinde yer alacağı grup.
     * 
     * Default boş olup ana grup ( main ) içerisinde yer alır.
     * @return 
     */
    String group() default "main";
    
    /**
     * Sıra numarası. Ardından isim sırasına dizilirler.
     * @return 
     */
    int order() default 10;
    
}
