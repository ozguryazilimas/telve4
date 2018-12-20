package com.ozguryazilim.telve.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;

/**
 * Parametre tipi veri giriş control sınıflarını işaretlemek için kullanılır.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@GroupedConversationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface ParamEdit {
    
}
