package com.ozguryazilim.telve.calendar;

import java.io.Serializable;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.util.ProxyUtils;

/**
 *
 * @author oyas
 */
public abstract class AbstractCalendarEventSource implements com.ozguryazilim.telve.calendar.CalendarEventSource, Serializable{

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    /**
     * Dialog için sınıf annotationı üzerinden aldığı Page ID'sini döndürür.
     *
     * @return
     */
    public String getDialogPageViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getDialogPage()).getViewId();
    }

    /**
     * Sınıf işaretçisinden @Lookup page bilgisini alır
     *
     * @return
     */
    public Class<? extends ViewConfig> getDialogPage() {
        return ((com.ozguryazilim.telve.calendar.annotations.CalendarEventSource)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(com.ozguryazilim.telve.calendar.annotations.CalendarEventSource.class))).dialogPage();
    }
    
    
    @Override
    public void createEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
