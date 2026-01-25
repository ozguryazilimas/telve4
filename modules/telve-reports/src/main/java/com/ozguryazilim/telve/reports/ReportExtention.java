package com.ozguryazilim.telve.reports;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

/**
 * Report ile işaretlenmiş sınıfları toparlayıp ReportRegistery'e yerleştirir.
 * @author Hakan Uygun
 */
public class ReportExtention implements Extension{
    /**
     * SubView ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(Report.class) ProcessAnnotatedType<T> pat) {

        Report a = pat.getAnnotatedType().getAnnotation(Report.class);
        String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
        
        //name.to
        ReportRegistery.register( name, a);
    }
    
}
