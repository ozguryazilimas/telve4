package com.ozguryazilim.telve.notify;

import com.ozguryazilim.telve.notification.NotificationService;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;

/**
 * PushNotification Handler.
 * 
 * OmniFaces socket üzerinden push notification gönderir.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class NotifyHandler {
    
    private Boolean pushEnabled = Boolean.FALSE;
    
    @Inject @Push(channel="notify")
    private PushContext notifyPushContext;
    
    @Inject
    private NotificationService notificationService;
    
    @PostConstruct
    public void init(){
        pushEnabled = "true".equals(ConfigResolver.getProjectStageAwarePropertyValue("notification.push.enabled", "false"));
    }
    
    
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
     * Omnifaces socket üzerinden push notify mesajı gönderir.
     * @param message 
     */
    public void sendMessage(NotifyMessage message){
        
        
        if( pushEnabled ){
            notifyPushContext.send(message, message.getTo());
        }
    }

    public Boolean getPushEnabled() {
        return pushEnabled;
    }
    
}
