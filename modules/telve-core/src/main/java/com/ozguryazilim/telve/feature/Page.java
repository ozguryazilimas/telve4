package com.ozguryazilim.telve.feature;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Page tanım annotation'ı
 * 
 * @author Hakan Uygun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(Pages.class)
public @interface Page {
    
    PageType type();
    
    Class<? extends ViewConfig> page();
    
}
