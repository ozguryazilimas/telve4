package com.ozguryazilim.telve.channel.sms;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.channel.Channel;
import com.ozguryazilim.telve.contact.Contact;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.activation.DataHandler;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

/**
 * SMS Mesajları için kanal
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named
public class SmsChannel implements Channel, Serializable{

    @Inject @ContextName("telve")
    @Uri("seda:smsChannel")
    private ProducerTemplate smsProducer; 
    
    
    @Override
    public void sendMessage(String to, String subject, String message) {
        sendMessage(to, subject, message, "GENERIC");
    }
    
    public void sendMessage( String to, String subject, String message, String messageClass ){
        Map<String, Object> headers = new HashMap<>();
        
        headers.put("target", to);
        headers.put("subject", subject);
        headers.put("messageClass", messageClass);
        
        smsProducer.sendBodyAndHeaders(message, headers);
    }

    @Override
    public void sendMessage(String to, String subject, String message, Map<String, Object> params) {
        Map<String, Object> headers = new HashMap<>();
        
        headers.put("target", to);
        headers.put("subject", subject);
        headers.put("messageClass", "GENERIC");
        
        for( Map.Entry<String,Object> e : params.entrySet()){
            headers.put(e.getKey(), e.getValue());
        }
        
        smsProducer.sendBodyAndHeaders(message, headers);
    }

    @Override
    public void sendMessage(Contact contact, String subject, String message, Map<String, Object> params) {
        Map<String, Object> headers = new HashMap<>();
        
        
        headers.put("subject", subject);
        headers.put("messageClass", "GENERIC");
        
        for( Map.Entry<String,Object> e : params.entrySet()){
            headers.put(e.getKey(), e.getValue());
        }
        
        //Kullanıcı user telefon nosuna gidecek
        headers.put("target", contact.getMobile());
        
        smsProducer.sendBodyAndHeaders(subject, headers);
    }

    @Override
    public boolean isValidContact(Contact contact) {
        return !Strings.isNullOrEmpty( contact.getMobile());
    }

    /**
     * SMS için attachment'lar kullanılmıyor
     * 
     * @param contact
     * @param subject
     * @param message
     * @param params
     * @param attachments 
     */
    @Override
    public void sendMessage(Contact contact, String subject, String message, Map<String, Object> params, Map<String, DataHandler> attachments) {
        sendMessage(contact, subject, message, params);
    }

    
}
