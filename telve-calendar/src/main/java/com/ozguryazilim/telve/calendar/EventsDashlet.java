/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.ozguryazilim.telve.dashboard.AbstractDashlet;
import com.ozguryazilim.telve.dashboard.Dashlet;
import com.ozguryazilim.telve.dashboard.DashletCapability;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.joda.time.LocalDate;
import org.primefaces.model.ScheduleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Yakın tarihte yapılması gereken olayları sunar.
 * 
 * @author Hakan Uygun
 */
@Dashlet(capability = {DashletCapability.canHide, DashletCapability.canMinimize, DashletCapability.canRefresh})
public class EventsDashlet extends AbstractDashlet{
    
    private static final Logger LOG = LoggerFactory.getLogger( EventsDashlet.class);
    
    @Inject
    private CalendarFilterModel filterModel;
    
    private LocalDate date;
    List<ScheduleEvent> todayEvents;
    List<ScheduleEvent> tomorrowEvents;
    List<ScheduleEvent> soonEvents;

    @Override
    public void load() {
        date = new LocalDate();
    }

    @Override
    public void refresh() {
        todayEvents = null;
        tomorrowEvents = null;
        soonEvents = null;
    }

    
    
    public Date getDate() {
        return date.toDate();
    }

    public List<ScheduleEvent> getTodayEvents(){
        if( todayEvents == null ){
            todayEvents = new ArrayList<>();
            populateEvents(todayEvents, date.toDate(), date.plusDays(1).toDate());
            //todayEvents = eventStore.getEvents(date.toDate(), date.plusDays(1).toDate());
        }
        return todayEvents;
    }
    
    public List<ScheduleEvent> getTomorrowEvents(){
        if( tomorrowEvents == null ){
            tomorrowEvents = new ArrayList<>();
            populateEvents(tomorrowEvents, date.plusDays(1).toDate(), date.plusDays(2).toDate());
            //tomorrowEvents = eventStore.getEvents(date.plusDays(1).toDate(), date.plusDays(2).toDate());
        }
        return tomorrowEvents;
    }
    
    public List<ScheduleEvent> getSoonEvents(){
        if( soonEvents == null ){
            soonEvents = new ArrayList<>();
            populateEvents(soonEvents, date.plusDays(2).toDate(), date.plusDays(7).toDate());
            //soonEvents = eventStore.getEvents(date.plusDays(2).toDate(), date.plusDays(7).toDate());
        }
        return soonEvents;
    }
    
    public void nextDay(){
        date = date.plusDays(1);
        refresh();
    }
    
    public void prevDay(){
        date = date.minusDays(1);
        refresh();
    }
    
    public void toDay(){
        date = new LocalDate();
        refresh();
    }

    public LocalDate getDateTime() {
        return date;
    }
    
    protected void populateEvents( List<ScheduleEvent> list, Date start, Date end ){
        for( String s : filterModel.getCalendarSources() ){
            try{
                CalendarEventSource cec = (CalendarEventSource) BeanProvider.getContextualReference(s);
                //list.addAll( cec.getEvents(start, end));
            } catch ( Exception e ){
                LOG.warn("Event Source not found {}", s);
            }
        }
    }

    public void onEventSelect(CalendarEventMetadata metadata) {
        CalendarEventSource cec = (CalendarEventSource) BeanProvider.getContextualReference(metadata.getSourceName());
        //cec.process( metadata );
    }
}
