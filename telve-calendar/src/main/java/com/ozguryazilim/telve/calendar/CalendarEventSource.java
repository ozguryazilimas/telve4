/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import java.util.Date;
import java.util.List;

/**
 * Olay kaynakları ( CalendarEventSource ile işaretlenmiş ) sınıflar için temel API.
 * 
 * @author Hakan Uygun
 */
public interface CalendarEventSource {
    
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
    void process( CalendarEventModel event );
    
    List<CalendarEventModel> getEvents( Date start, Date end );
}
