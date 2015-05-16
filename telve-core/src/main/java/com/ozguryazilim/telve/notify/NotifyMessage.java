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
    private String icon = "fa-bell-o";
    private String severity = "info";
    private String link;

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

    public NotifyMessage(String to, String subject, String body, String link, String icon, String severity) {
        this.subject = subject;
        this.body = body;
        this.link = link;
        this.to = to;
        this.icon = icon;
        this.severity = severity;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    
    
    @Override
    public String toString() {
        return "NotifyMessage{" + "to=" + to + ", subject=" + subject + ", body=" + body + ", icon=" + icon + ", severity=" + severity + '}';
    }
    
    
    
}
