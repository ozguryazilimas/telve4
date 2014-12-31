/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar.simple;

import java.io.Serializable;

/**
 * Takvim sistemi için simple bir event model. 
 * 
 * Standart event olarak telve taksim sistemi içerisine gelir.
 * 
 * Geri kalan veriler CalendarEvent modeli içerisinde yer alır.
 * 
 * @author Hakan Uygun
 */
public class SimpleEvent implements Serializable{
    
    private String location;
    private String status;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
