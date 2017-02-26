/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Takvim olayları için kaynak arayüzü.
 * 
 * Farklı sistemlerden takvim'e içerik göndermek için bir arayüz. Her içerik kaynağı geri dönüşler için bazı tanımları yapmak durumunda.
 * 
 * Bazı şeyler namingConvention ile bulunur.
 * 
 * SourceName: Sınıf ismi olacaktır. UI üzerinde calendar.source.name.{name} olarak çözümlenecek
 * Style : Her kaynak için farklı renk kodları için calendar.source.style.{name} olarak çözümlenecek
 * 
 * @author Hakan Uygun
 */
@Stereotype
@Dependent
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface CalendarEventSource {

    /**
     * Kullanıcı tarafından oluşturulup oluşturulamıyacağı.
     * 
     * eğer true olursa Calendar New düğmesine eklenir.
     * 
     * @return 
     */
    boolean creatable() default false;
    
    /**
     * Event için bir dialog olup olmadığı.
     * 
     * Eğer yoksa control sınıfı tarafından redirect edilmesi istenir.
     * 
     * @return 
     */
    boolean hasDialog() default false;
    
    /**
     * Eğer dilog bulunmuyorsa bu değer dikkate alınmaz. Eğer dialog varsa mutlaka doldurulması gerekir.
     * @return 
     */
    Class<? extends ViewConfig> dialogPage() default ViewConfig.class;
    
    /**
     * Bu kaynağın eventlerinin nasıl sunulacağını tanımlar.
     * 
     * Varsayılan hali boştur. tlv-yellow, tlv-green gibi hazır değerler olabilir.
     * 
     * @return 
     */
    String styleClass() default "";
}
