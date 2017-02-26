/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import java.util.Date;
import org.primefaces.model.LazyScheduleModel;

/**
 * Calendar'da gösterilecek olan eventler için saklama API'si.
 * 
 * Bu interface'i implemente eden çeşitli sınıflar farklı veri kaynaklarından calendar event'i üretebilir.
 * 
 * Ex: DefaultCalendarEventStore, ICalCalendarEventStore v.b.
 * 
 * 
 * 
 * @author Hakan Uygun
 */
public interface CalendarEventStore {
   
    /**
     * Store için unique bir isim. 
     * 
     * UI üzerinde calendar.store.${storeName} olarak aranacak. dil dosyalarına bu şekilde konmalı
     * 
     * @return 
     */
    String getStoreName();
    
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
    
}
