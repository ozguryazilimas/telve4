package com.ozguryazilim.telve.notify;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Notification sistemei için mesaj model sınıfı.
 * 
 * @author Hakan Uygun
 */
public class NotifyMessage implements Serializable{
   
    private String id;
    private String to = "*";
    private String subject;
    private String body;
    private String icon = "fa-bell-o";
    private String severity = "info";
    private String link;

    public NotifyMessage() {
        this.id = UUID.randomUUID().toString();
    }

    
    public NotifyMessage(String to, String subject, String body) {
        this.id = UUID.randomUUID().toString();
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public NotifyMessage(String subject, String body) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.body = body;
    }

    public NotifyMessage(String to, String subject, String body, String link, String icon, String severity) {
        this.id = UUID.randomUUID().toString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NotifyMessage other = (NotifyMessage) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NotifyMessage{" + "to=" + to + ", subject=" + subject + ", body=" + body + ", icon=" + icon + ", severity=" + severity + '}';
    }
    
    
    
}
