/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notify;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.apache.commons.lang3.StringEscapeUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 * PFP Notification mesajları için API
 * 
 * @author Hakan Uygun
 */
@RequestScoped
@Named
public class NotifyHandler {
    
    private final static String CHANNEL = "/notify/";
    
    /**
     * Sistemde login olan herkese mesaj gönderir.
     * @param message 
     */
    public void sendMessage(String message){
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(CHANNEL + "hakan", new FacesMessage(StringEscapeUtils.escapeHtml4(message), StringEscapeUtils.escapeHtml4(message)));
        //eventBus.publish(CHANNEL + "hakan", StringEscapeUtils.escapeHtml4(message));
    }
    
}
