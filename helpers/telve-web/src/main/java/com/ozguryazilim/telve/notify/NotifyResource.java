/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notify;

import javax.faces.application.FacesMessage;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 * PFP ile Notification Tanımı.
 * 
 * 
 * http://code.google.com/p/primefaces/issues/detail?id=7011 nedeniyle telve-core yerine telve-web içerisinde
 * 
 * https://code.google.com/p/primefaces/issues/detail?id=7469 nedeniyle OnOpen ve OnClose'lar var.
 * 
 * @author Hakan Uygun
 */
@PushEndpoint("/notify/{username}")
public class NotifyResource {
    
    @PathParam("username")
    private String username;
    
    
    @OnMessage(encoders = {JSONEncoder.class})
    public FacesMessage onMessage(FacesMessage message) {
        return message;
    }
    
    /*
    @OnMessage(encoders = {JSONEncoder.class})
    public String onMessage(String message) {
        return message;
    }*/
    
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
    }
}
