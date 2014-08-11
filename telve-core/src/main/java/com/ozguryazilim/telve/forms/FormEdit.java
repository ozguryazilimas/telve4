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
 * Normal Formlar için View ve Edit form View Controller sınıfını işaretler.
 * @author Hakan Uygun
 */
@Stereotype
@GroupedConversationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface FormEdit {
    
    /**
     * Browse view'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> browsePage();
    /**
     * Edit View'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> editPage();
    /**
     * View View'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> viewContainerPage();
    /**
     * Master Sub View'in hangisi olduğu
     * @return 
     */
    Class<? extends ViewConfig> masterViewPage();
    
}
