/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.WindowScoped;

/**
 * Komut editor sınıfları için işaretçi.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@WindowScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface CommandEditor {
    
    /**
     * Editörün hangi komut için çalışacağı
     * @return 
     */
    Class<? extends StorableCommand> command();
    
    /**
     * İlgili editör için açılacak sayfa yolu
     * @return 
     */
    Class<? extends ViewConfig> page();
    
    /**
     * Editor için yetki konfigürasyonu.
     * 
     * Tanımlanmazsa sınıf adını kullanır.
     * 
     * @return 
     */
    String permission() default "";
    
    /**
     * Editor için kategori tanımlanması
     * 
     * @return 
     */
	String category() default "Default";
    
    
}
