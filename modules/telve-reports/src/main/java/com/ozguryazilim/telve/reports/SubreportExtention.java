package com.ozguryazilim.telve.reports;

import com.google.common.base.CaseFormat;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subreport ile işaretlenmiş sınıfları toparlayıp SubreportRegistery'e
 * yerleştirir.
 *
 * @author Hakan Uygun
 */
public class SubreportExtention implements Extension {

    private static final Logger LOG = LoggerFactory.getLogger(SubreportExtention.class);
    
    /**
     * SubView ile işaretli sınıfları bulup SubViewRegistery'e yerleştirir.
     *
     * @param <T>
     * @param pat
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(Subreport.class) ProcessAnnotatedType<T> pat) {

        if (AbstractSubreportBase.class.isAssignableFrom(pat.getAnnotatedType().getJavaClass())) {
            Subreport a = pat.getAnnotatedType().getAnnotation(Subreport.class);
            String name = pat.getAnnotatedType().getJavaClass().getSimpleName();

            //Conver CDI bean name
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
            
            SubreportRegistery.register(name, a);
        } else {
            LOG.error("{} is not extends AbstractSubreportBase. This subreport cannot be used.", pat.getAnnotatedType().getJavaClass().getName());
        }
    }
}
