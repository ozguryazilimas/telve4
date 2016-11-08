/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.feature.FeatureHandler;
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
 * Browse türü kontrol sınıflarını işaretler.
 * @author Hakan Uygun
 */
@Stereotype
@WindowScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface Browse {
    /**
     * Browse view'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> browsePage() default ViewConfig.class;
    /**
     * Edit View'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> editPage() default ViewConfig.class;
    /**
     * View View'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> viewContainerPage() default ViewConfig.class;
    
    Class<? extends FeatureHandler> feature() default FeatureHandler.class;
}
