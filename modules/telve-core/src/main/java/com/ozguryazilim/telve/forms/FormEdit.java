package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.feature.FeatureHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Named;
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
    
    Class<? extends FeatureHandler> feature();
}
