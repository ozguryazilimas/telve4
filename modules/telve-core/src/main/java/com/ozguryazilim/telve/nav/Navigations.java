/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.nav;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.deltaspike.core.api.config.view.metadata.ViewMetaData;

/**
 * Bir viewConfig'e birden fazla navisgosyon vermek için kullanılır.
 * @author Hakan Uygun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ViewMetaData
public @interface Navigations {
    
    /**
     * Navigation itemları
     * @return 
     */
    Navigation[] value();
    
}
