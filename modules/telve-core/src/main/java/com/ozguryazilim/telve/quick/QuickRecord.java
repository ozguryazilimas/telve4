package com.ozguryazilim.telve.quick;

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
 * QuickRecord bileşenlerini işaretler.
 * @author Hakan Uygun
 */
@Stereotype
@GroupedConversationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface QuickRecord {
    
    /**
     * View için kullanılacak olan xhtml yolu.
     * 
     * @return 
     */
    Class<? extends ViewConfig> page();
    
    /**
     * QuickRecord çalıştırma yetkisinin ne olduğu.
     *
     * Eğer verilmezse Sınıf adını kullanır.
     * @return
     */
    String permission() default "";
    
    /**
     * Sıra numarası. Ardından isim sırasına dizilirler.
     * @return 
     */
    int order() default 10;
    
    boolean showonMenu() default true;
}
