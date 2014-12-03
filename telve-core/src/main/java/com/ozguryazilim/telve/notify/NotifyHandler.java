/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notify;

import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
@Named
public class NotifyHandler {
    
    private final static String CHANNEL = "/notify/";
    
    /**
     * Sistemde login olan herkese mesaj gönderir.
     * @param message 
     */
    public void sendMessage(String message){
        NotifyMessage nm = new NotifyMessage();
        nm.setSubject(message);
        nm.setBody(message);
        sendMessage(nm);
    }
    
    /**
     * Sistemde login olan herkese mesaj gönderir.
     * @param subject
     * @param message 
     */
    public void sendMessage(String subject, String message){
        NotifyMessage nm = new NotifyMessage( subject, message);
        
        sendMessage(nm);
    }
    
    /**
     * Sistemde login olan kullanıcıya mesaj gönderir.
     * @param to Mesajın gönderileceği kullanıcı
     * @param message 
     * @param subject 
     */
    public void sendMessage(String to, String subject, String message){
        NotifyMessage nm = new NotifyMessage( to, subject, message);
        
        sendMessage(nm);
    }
    
    /**
     * Atmosfer üzerinden push Notify mesajı gönderir.
     * @param message 
     */
    public void sendMessage(NotifyMessage message){
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(CHANNEL + message.getTo(), new FacesMessage(StringEscapeUtils.escapeHtml4(message.getSubject()), StringEscapeUtils.escapeHtml4(message.getBody())));
        //eventBus.publish(CHANNEL + "hakan", StringEscapeUtils.escapeHtml4(message));
    }
}
