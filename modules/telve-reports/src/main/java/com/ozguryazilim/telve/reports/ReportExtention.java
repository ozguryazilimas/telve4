package com.ozguryazilim.telve.reports;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

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
