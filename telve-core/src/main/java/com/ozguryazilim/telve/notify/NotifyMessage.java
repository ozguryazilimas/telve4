/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notify;

import java.io.Serializable;

/**
 * Notification sistemei için mesaj model sınıfı.
 * 
 * @author Hakan Uygun
 */
public class NotifyMessage implements Serializable{
   
    private String to = "*";
    private String subject;
    private String body;

    public NotifyMessage() {
        
    }

    
    public NotifyMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public NotifyMessage(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    
    
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NotifyMessage{" + "to=" + to + ", subject=" + subject + ", body=" + body + '}';
    }
    
    
    
}
