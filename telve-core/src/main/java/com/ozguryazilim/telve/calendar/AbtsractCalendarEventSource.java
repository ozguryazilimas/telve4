/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ozguryazilim.telve.entities.CalendarEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.util.ProxyUtils;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;

/**
 * DefaultCalendarEventStore kullanacak olan kaynaklar için taban sınıf.
 * 
 * @author Hakan Uygun
 * @param <E> EventData model sınıf
 */
public abstract class AbtsractCalendarEventSource<E> implements CalendarEventController, Serializable{

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private CalendarEventRepository repository;
    
    private CalendarEvent calendarEvent;
    private E data;
    
    /**
     * CalendarEvent içerisinde json olarak bulunan veriyi sınıfa tanırılmış modele çevirir.
     * 
     * @param event
     * @return 
     */
    public E getContentData( CalendarEvent event){
        Gson gson = new Gson();
        return gson.fromJson(event.getSourceData(), new TypeToken<E>(){}.getType());
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
        
        e.setStyleClass("calendar.source.style."+getClass().getSimpleName());
        e.setData(getContentData(event));
        
        return e;
    }

    @Override
    public void createEvent() {
        createNewModel();
        openDialog();
    }

    @Override
    public void process(CalendarEvent event) {
        //FIXME: Burada kontrolün becerisine bakıp redirect edilebilir.
        this.setCalendarEvent(event);
        this.setData( getContentData(event));
        openDialog();
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
        
        RequestContext.getCurrentInstance().closeDialog(null);
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
    
    
}
