package com.ozguryazilim.telve.lookup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Lookup Dialog controller sınıflarını işaretler.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@SessionScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface Lookup {
   
    /**
     * Dialog için kullanılacak view'in hangisi olduğu
     * 
     * @return 
     */
    Class<? extends ViewConfig> dialogPage(); 
}
