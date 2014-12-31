/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.ozguryazilim.telve.entities.CalendarEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 * Varsayılan Takvim olay deposu.
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class DefaultCaledarEventStore implements CalendarEventStore, Serializable{

    
    @Inject
    private CalendarEventRepository repository;
    
    @Override
    public String getStoreName() {
        return "DefaultCaledarEventStore";
    }

    @Override
    public void loadEvents(LazyScheduleModel model, Date start, Date end) {
        
        List<CalendarEvent> events = repository.findFilteredEvents(start, end);
        for( CalendarEvent e : events ){
            model.addEvent(getScheduleEvent(e));
        }
    }
    
    public List<ScheduleEvent> getEvents( Date start, Date end ){
        List<CalendarEvent> events = repository.findFilteredEvents(start, end);
        List<ScheduleEvent> result = new ArrayList<>();
        
        for( CalendarEvent e : events ){
            result.add(getScheduleEvent(e));
        }
        
        return result;
    } 
    
    /**
     * Verilen CalendarEvent modelinden DefaultScheduleEvent üretir.
     * @param event
     * @return 
     */
    public DefaultScheduleEvent getScheduleEvent( CalendarEvent event ){
        DefaultScheduleEvent e = new DefaultScheduleEvent();
        
        e.setTitle(event.getTitle());
        e.setDescription(event.getInfo());
        e.setAllDay(event.getAllDay());
        e.setEditable(event.getEditable());
        e.setStartDate(event.getStartDate());
        e.setEndDate(event.getEndDate());
        
        e.setStyleClass(CalendarEventSourceRegistery.getMetadata(event.getSourceName()).styleClass());
        e.setData(event);
        
        return e;
    }
}
