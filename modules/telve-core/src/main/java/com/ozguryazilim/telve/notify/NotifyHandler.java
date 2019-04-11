package com.ozguryazilim.telve.notify;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * PushNotification Handler.
 * 
 * FIXME: PF üzerinden kalktığı için şimdilik temizliyoruz. 
 * Aslında bütün bu süreç SMS Channel gibi tamamen kendine ait bir optinal modülegitmeli.
 * 
 * NotifyChannelDispacher için bir interface üzerinden produce edilmeli ve istenilir ise kullanılabilmeli.
 * 
 * Burada Push Notification kavramı aslında çeşitli IM implementasyonları bile olabilir.
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
        //EventBus eventBus = EventBusFactory.getDefault().eventBus();
        //eventBus.publish(CHANNEL + message.getTo(), new JSONObject(message).toString());
        //eventBus.publish(CHANNEL + "hakan", StringEscapeUtils.escapeHtml4(message));
    }
}
