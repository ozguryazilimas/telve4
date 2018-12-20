package com.ozguryazilim.telve.nav;

import com.ozguryazilim.telve.feature.FeatureHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.deltaspike.core.api.config.view.metadata.ViewMetaData;

/**
 * Page tanımlarının navigation üzerinde nasıl yer alacağını tanımlamak için kullanılır.
 * 
 * DeltaSpike view tanımları için meta data olarak eklenir.
 * 
 * Aynı view birden fazla yerde gösterilecek ise birden fazla navigation tanımlanabilir.
 * 
 * @author Hakan Uygun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ViewMetaData
@Repeatable(Navigations.class)
public @interface Navigation {
   
    /**
     * UI üzerinde gösterilecek olan metin.
     * 
     * Dil desteği için key verilebilir.
     * @return 
     */
    String label() default "";
    
    /**
     * UI üzerinde gösterilecek olan icon.
     * 
     * / ile başlarsa png resim olacak aksi halde fonticon olarak kabul edilecek ve sınıf olarak eklenecek.
     * 
     * eğer boş ise nav.icon.Sınıf ismi olacak
     * 
     * @return 
     */
    String icon() default "";
    
    /**
     * UI üzerinde hangi grup içerisinde gösterileceği.
     * 
     * Bu da label gibi key olabilir.
     * 
     * Özel iki tane durum var: 
     * 
     * MAIN   : Ana navigasyon olarak üst bar menüde gösterilir.
     * SIDE   : Sidebar üzerinde bagımsız üst menu olarak gösterilir.
     * 
     * @return 
     */
    Class< ? extends NavigationSection> section();
    
    /**
     * UI üzerinde hangi sırada görüneceği.
     * 
     * Bu sıranın ardından isim sırasına dizilirler.
     * 
     * @return 
     */
    int  order() default 50;
    
    Class<? extends FeatureHandler> feature() default FeatureHandler.class;
}
