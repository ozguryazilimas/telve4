/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.ozguryazilim.telve.calendar.annotations.CalendarEventSource;
import com.google.gson.Gson;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.CalendarEvent;
import com.ozguryazilim.telve.reminder.ReminderService;
import com.ozguryazilim.telve.utils.DateUtils;
import com.ozguryazilim.telve.utils.ScheduleModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.util.ProxyUtils;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 * DefaultCalendarEventStore kullanacak olan kaynaklar için taban sınıf.
 * 
 * @author Hakan Uygun
 * @param <E> EventData model sınıf
 */
public abstract class AbtsractStorableCalendarEventSource<E> implements com.ozguryazilim.telve.calendar.CalendarEventSource, Serializable{

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    protected CalendarEventRepository repository;
    
    @Inject
    protected ReminderService reminderService;

    /**
     * Context üzerinde tanımlı filtre
     */
    @Inject
    private CalendarFilterModel filterModel;
    
    @Inject
    private Identity identity;
    
    private CalendarEvent calendarEvent;
    private E data;
    
    
    /**
     * Json'u geri parse etmek için sınıf lazım.
     * 
     * Daha iyi bir yol bulana kadar şimdilik böyle.
     * 
     * UI kısmı olmasa Map kullanmak lazım diye düşündüm? Belki de StoredCommand yapısını kullanırız.
     * 
     */
    public abstract Class<E> getPayloadClass();
    
    /**
     * CalendarEvent içerisinde json olarak bulunan veriyi sınıfa tanırılmış modele çevirir.
     * 
     * @param event
     * @return 
     */
    public E getContentData( CalendarEvent event ){
        Gson gson = new Gson();
        return gson.fromJson(event.getSourceData(), getPayloadClass() );
    }
    
    /**
     * Verilen model verisini CalendarEvent#sourceData içine json olarak yerleştirir.
     * @param event
     * @param data 
     */
    public void setContentData( CalendarEvent event, E data ){
        Gson gson = new Gson();
        event.setSourceData(gson.toJson(data));
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
        
        e.setStyleClass( filterModel.getSourceStyle(getEventSourceName()));
        CalendarEventMetadata meta = new CalendarEventMetadata();
        meta.setSourceKey(event.getSourceKey());
        meta.setSourceName(event.getSourceName());
        e.setData(meta);
        
        return e;
    }

    @Override
    public void createEvent() {
        createNewModel();
        openDialog();
    }

    @Override
    public void process(CalendarEventMetadata event) {
        //FIXME: Burada Tavır tamamen değişmeli.
        //this.setCalendarEvent(event);
        //this.setData( getContentData(event));
        //openDialog();
    }
    
    
    
    /**
     * Yeni vent oluşturmadan önce çağrılır.
     */
    public abstract void createNewModel();
    
    /**
     * Geriye açılacak olan popup için view adı döndürür.
     *
     * Bu view dialogBase sınıfından türetilmiş olmalıdır.
     *
     *
     * @return
     */
    public String getDialogName() {
        String viewId = getDialogPageViewId();
        return viewId.substring(0, viewId.indexOf(".xhtml"));
    }

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
        return ((CalendarEventSource)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(CalendarEventSource.class))).dialogPage();
    }
    
    
    /**
     * EventSource için dialog açar.
     *
     * Dialog çağırılmadan önce ilgili model düzenlenmelidir.
     * 
     */
    public void openDialog() {
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        //options.put("contentHeight", 450);

        RequestContext.getCurrentInstance().openDialog(getDialogName(), options, null);
    }

    @Transactional
    public void closeDialog(){
        
        setContentData(calendarEvent, data);
        repository.save(calendarEvent);
        
        //Reminder Settings
        Date d = DateUtils.getDateBeforePeriod(calendarEvent.getReminderSchedule(), calendarEvent.getStartDate());
        reminderService.createReminder(calendarEvent.getTitle(), calendarEvent.getReminderTarget(), ScheduleModel.getOnceExpression(d));
        
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    @Transactional
    public void save( CalendarEvent event ){
        repository.save(event);
    }
    
    public void cancelDialog(){
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    /**
     * Dialoglarda kulanılmak için veri modeli 
     * @return 
     */
    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }

    public void setCalendarEvent(CalendarEvent calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

    /**
     * Dialoglarda kullanılmak için ek veri modeli
     * @return 
     */
    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

          
    @Override
    public void loadEvents(LazyScheduleModel model, Date start, Date end) {
        
        List<ScheduleEvent> events = getEvents( start, end );
        
        for( ScheduleEvent e : events ){
            model.addEvent(e);
        }
    }
    
    @Override
    public List<ScheduleEvent> getEvents( Date start, Date end ){
        List<CalendarEvent> events = repository.findFilteredEvents(start, end, getEventSourceName(), identity.getLoginName(), identity.getUnifiedRoles(), filterModel.getShowPersonalEvents(), filterModel.getShowClosedEvents());
        List<ScheduleEvent> result = new ArrayList<>();
        
        for( CalendarEvent e : events ){
            result.add(getScheduleEvent(e));
        }
        
        return result;
    }
    
    /**
     * Sınıf adından event Source adnına dönüştürüyoruz.
     * @return 
     */
    protected abstract String getEventSourceName();
    
}
