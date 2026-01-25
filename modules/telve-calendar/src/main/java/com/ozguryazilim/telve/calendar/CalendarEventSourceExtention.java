package com.ozguryazilim.telve.calendar;

import com.ozguryazilim.telve.calendar.annotations.CalendarEventSource;
import com.google.common.base.CaseFormat;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

/**
 * CalendarEventSource ile işretlenmiş sınfıları tarar ve registery'e yerleştirir.
 * 
 * @author Hakan Uygun
 */
public class CalendarEventSourceExtention implements Extension{
    
    /**
     * calendarEventSource ile işaretli sınıfları bulup Registery'e yerleştirir.
     * @param <T>
     * @param pat 
     */
    <T> void processAnnotatedType(@Observes @WithAnnotations(CalendarEventSource.class) ProcessAnnotatedType<T> pat) {

        CalendarEventSource a = pat.getAnnotatedType().getAnnotation(CalendarEventSource.class);
        String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, pat.getAnnotatedType().getJavaClass().getSimpleName());
        
        CalendarEventSourceRegistery.register( name, a);
    }
}
