/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Takvim arayüzlerinden setlenecek filtre modeli.
 *
 * Bu model context üzerinde yaşar. Kullanıcın seçimleri kahve içerisinde
 * saklanır.
 *
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class CalendarFilterModel implements Serializable {

    @Inject
    @UserAware
    private Kahve kahve;

    private List<String> calendarSources;
    private Boolean showPersonalEvents;
    private Boolean showClosedEvents;

    public List<String> getCalendarSources() {

        if (calendarSources == null) {
            KahveEntry o = kahve.get("calendar.filter.sources");
            //Kullanıcı için önceden tanımlanmış bir liste var mı?
            if( o != null){
                calendarSources = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(o.getAsString());
            }
            
            //Yoksa ya da boşsa sistemdekilerin hepsi.
            if (calendarSources == null || calendarSources.isEmpty()) {
                calendarSources = CalendarEventSourceRegistery.getRegisteredEventSources();
            }
        }

        return calendarSources;
    }

    public void setCalendarSources(List<String> calendarSources) {
        this.calendarSources = calendarSources;
        String s = Joiner.on(',').join(calendarSources);
        kahve.put("calendar.filter.sources", s);
    }

    public Boolean getShowPersonalEvents() {
        if (showPersonalEvents == null) {
            KahveEntry o = kahve.get("calendar.filter.ShowPOT", Boolean.FALSE);
            showPersonalEvents = o.getAsBoolean();
        }
        return showPersonalEvents;
    }

    public void setShowPersonalEvents(Boolean showPersonalEvents) {
        this.showPersonalEvents = showPersonalEvents;
        kahve.put("calendar.filter.ShowPOT", showPersonalEvents);
    }

    public Boolean getShowClosedEvents() {

        if (showClosedEvents == null) {
            KahveEntry o = kahve.get("calendar.filter.ShowClosed", Boolean.FALSE);
            showClosedEvents = o.getAsBoolean();
        }

        return showClosedEvents;
    }

    public void setShowClosedEvents(Boolean showClosedEvents) {
        this.showClosedEvents = showClosedEvents;
        kahve.put("calendar.filter.ShowClosed", showClosedEvents);
    }

    /**
     * Geriye tanımlı event source listesini döndürür.
     *
     * @return
     */
    public List<String> getRegisteredEventSources() {
        return CalendarEventSourceRegistery.getRegisteredEventSources();
    }
}
