package com.ozguryazilim.telve.quick;

import com.google.common.base.CaseFormat;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

/**
 * Sistemde QuickRecord olarak işaretlenmiş bileşenleri tarar.
 * 
 * @author Hakan Uygun
 */
public class QuickRecordExtention implements Extension{
   
    /**
     * SubView ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(QuickRecord.class) ProcessAnnotatedType<T> pat) {

        QuickRecord a = pat.getAnnotatedType().getAnnotation(QuickRecord.class);
        String name = pat.getAnnotatedType().getJavaClass().getSimpleName();
        //Bean adına çevirelim.
        name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        
        //name.to
        QuickRecordRegistery.register( name, a);
    }
}
