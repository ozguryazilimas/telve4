/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * Bir modül içindeki bir özelliği sisteme tanıtmak için kullanıcak olan yapıdır. 
 * 
 * Link üretmek generic aramalar yapmak için yapı sağlar.
 * 
 * FeatureHandler interface'ni implemete eden bir sınıf üzerinde bulunur.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@SessionScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface Feature {

    /**
     * Özellik için kullanılacak genel isim. Message bundle'da aranır 
     * 
     * Örneğin : fetaure.caption.SimpleFeature
     * 
     * varsayılan olarak fetaure.caption.FatureName döner
     * 
     * @return 
     */
    String caption() default "";
    
    /**
     * Özellik için kullanılcak olan permission domain
     * 
     * @return 
     */
    String permission();
    
    /**
     * Her hangi bir domain sınıfı üzerinden taranabilmesi istenir ise
     * 
     * FeatureRegistery.for( getEntity().getClass()); şeklinde sorgulamalar için 
     * 
     * @return 
     */
    Class<?> forEntity() default Object.class;

    /**
     * Özelliğin ait olduğu kategoriler. FeatureCategory'den alınır.
     *
     * Örneğin: ACCOUNTABLE, NON_ACCOUNTABLE
     *
     * varsayılan olarak yalnızca NON_ACCOUNTABLE içeren bir array döner.
     *
     * @return
     */

    FeatureCategory[] category() default {FeatureCategory.NON_ACCOUNTABLE};
}
