/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

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
 * IDM için kullanıcı model bilgisini uygulama katmanından zenginleştirmek için extention işaretleyici.
 * 
 * imlementasyon için ... sınıfı kullanılır.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@Named
@GroupedConversationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface UserModel {
    
    /**
     * Kullanıcı tanımlama ekranına ek gelecek UI fragment id'si
     *
     * @return
     */
    Class<? extends ViewConfig> fragment();
    
    /**
     * Eğer sisteme yeni kullanıcı tipi eklencek ise onlar yazılır.
     * @return 
     */
    String[] userTypes() default {};
}
