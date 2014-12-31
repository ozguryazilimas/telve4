/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.ozguryazilim.telve.entities.CalendarEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calendar sistemi UI control sınıfı
 * @author Hakan Uygun
 */
@Named
@WindowScoped 
public class CalendarHome implements Serializable{
    private static final Logger LOG = LoggerFactory.getLogger(CalendarHome.class);
    
    @Inject 
    private DefaultCaledarEventStore caledarEventStore;
    
    private ScheduleModel model;
    private ScheduleEvent selectedEvent;

    @PostConstruct
    public void init(){
        model = new LazyScheduleModel(){

            @Override
            public void loadEvents(Date start, Date end) {
                caledarEventStore.loadEvents(this, start, end);
            }
            
        };
    }

    public ScheduleModel getModel() {
        return model;
    }

    public void setModel(ScheduleModel model) {
        this.model = model;
    }

    /**
     * GUI'den gelen newEvent komutunu 
     */
    public void newEvent(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String eventSource = params.get("eventSource");
        
        CalendarEventController cec = (CalendarEventController) BeanProvider.getContextualReference(eventSource);
        cec.createEvent();
    }
    
    public void editEvent(){
        CalendarEvent ce = (CalendarEvent)selectedEvent.getData();
        
        CalendarEventController cec = (CalendarEventController) BeanProvider.getContextualReference(ce.getSourceName());
        cec.process( ce );
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        selectedEvent = (ScheduleEvent) selectEvent.getObject();
    }
    
    public void onViewChange(SelectEvent selectEvent) {
        String s  = (String) selectEvent.getObject();
        System.out.println(s);
    }
    
}
