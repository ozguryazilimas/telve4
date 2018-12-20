package com.ozguryazilim.telve.reports;

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
 * Rapor Kontrol Sınıflarını işaretlemek için kullanılır.
 *
 * @author Hakan Uygun
 */
@Stereotype
@WindowScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface Report {

    /**
     * Jasper Reportlar için .jasper uzantılı rapor şablonu.
     *
     * Eğer verilmezse sınıf adını kullanır.
     * 
     * @return
     */
    String template() default "";

    /**
     * Rapor filtrelerinin alınacağı View'in hangisi olduğu
     *
     * @return
     */
    Class<? extends ViewConfig> filterPage();

    /**
     * Raporun menu'de hangi kırılım altında görüneceği
     *
     * @return
     */
    String path();

    /**
     * Raporun çalıştırma yetkisinin ne olduğu.
     *
     * Eğer verilmezse Sınıf adını kullanır.
     * @return
     */
    String permission() default "";
    
    /**
     * Eğer jasper report için dil dosyasının yolu
     *
     * @return
     */
    String resource() default "";

    /**
     * Rapor tipi
     *
     * @return
     */
    ReportType type() default ReportType.JasperReport;
    
    /**
     * Rapor için icon.
     * Varsayılan değer standart rapor iconudur.
     * icon ismi "/img/ribbon/large/" altında aranır.
     * @return 
     */
    String icon() default "report.png";
    
    /**
     * Sıra numarası. Ardından isim sırasına dizilirler.
     * @return 
     */
    int order() default 10;
}
