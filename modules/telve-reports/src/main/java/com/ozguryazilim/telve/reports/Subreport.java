package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.feature.FeatureHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;

/**
 *
 * DynamicReport ve benzerleri için subreport tanımlama için.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@WindowScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface Subreport {
    
    /**
     * Subreport tür ismi. 
     * 
     * Subreport seçimi sırasında bu isimle arama yapılacak.
     * 
     * @return 
     */
    String group();
    
    /**
     * Bu subreport'un hangi feature için olduğu.
     * 
     * Genelde feature için filtreleme yapılacak.
     * 
     * @return 
     */
    Class<? extends FeatureHandler> feature();
    
}
