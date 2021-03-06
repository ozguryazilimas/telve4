package com.ozguryazilim.telve.channel;

import com.ozguryazilim.telve.contact.Contact;
import java.util.Map;
import javax.activation.DataHandler;

/**
 * Channel implementasyonları için arayüz.
 * 
 * @author Hakan Uygun
 */
public interface Channel {
   
    /**
     * Basit mesajlar için
     * @param to
     * @param subject
     * @param message 
     */
    void sendMessage( String to, String subject, String message ); 
   
    /**
     * Biraz daha karmaşık mesajlar için
     * @param to
     * @param subject
     * @param message
     * @param params 
     */
    void sendMessage( String to, String subject, String message, Map<String, Object> params ); 
    
    /**
     * Verilen Contact'a mesaj gönderir.
     * 
     * Mesaj 
     * @param contact
     * @param subject
     * @param message
     * @param params 
     */
    void sendMessage( Contact contact, String subject, String message, Map<String, Object> params ); 
    
    /**
     * Verilen Contact'a mesaj gönderir.
     * 
     * Mesaj 
     * @param contact
     * @param subject
     * @param message
     * @param params 
     * @param attachments 
     */
    void sendMessage( Contact contact, String subject, String message, Map<String, Object> params,  Map<String, DataHandler> attachments ); 
    
    /**
     * Verilen contact'ın bu channel için geçerli ve yeterli bilgiye sahip olup olmadığını kontrol eder.
     * 
     * 
     * @param contact
     * @return gerçersiz ise false döner
     */
    boolean isValidContact( Contact contact );
}
