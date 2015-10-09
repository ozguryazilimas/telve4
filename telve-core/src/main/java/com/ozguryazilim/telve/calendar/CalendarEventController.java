/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import java.util.Date;
import java.util.List;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 * Olay kaynakları ( CalendarEventSource ile işaretlenmiş ) sınıflar için temel API.
 * 
 * @author Hakan Uygun
 */
public interface CalendarEventController {
    
    /**
     * Yeni event oluşturmak için çağrılır.
     * 
     * eğer creatable olarak işaretlenmemişse çağrılmaz.
     * 
     */
    void createEvent();
 
    /**
     * Duruma göre dialog açmak ya da redirect etmek için çalışır.
     * @param event 
     */
    void process( CalendarEventMetadata event );
    
    /**
     * Verilen model'in içine verilen tarih aralığına uygun eventler yerleştirilir.
     * 
     * Ex: model.addEvent(...)
     * 
     * @param model
     * @param start
     * @param end 
     */
    void loadEvents( LazyScheduleModel model, Date start, Date end );

    List<ScheduleEvent> getEvents( Date start, Date end );
}
