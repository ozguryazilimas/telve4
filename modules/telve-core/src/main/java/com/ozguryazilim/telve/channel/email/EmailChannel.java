package com.ozguryazilim.telve.channel.email;

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
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

/**
 * EmailChannel üzerinden mesaj göndermek için kullanılır.
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named
public class EmailChannel implements Channel, Serializable{
    
    @Inject @ContextName("telve")
    @Uri("seda:emailChannel")
    private ProducerTemplate mailProducer; 
    
    @Override
    public void sendMessage( String to, String subject, String message ){
        Map<String, Object> headers = new HashMap<>();
        
        headers.put("target", to);
        headers.put("subject", subject);
        headers.put("messageClass", "GENERIC");
        
        mailProducer.sendBodyAndHeaders(message, headers);
    }
    
    
    public void sendMessageWithAttachments( String to, String subject, String message, String messageClass, Map<String, DataHandler> attachments ){
        Map<String, Object> headers = new HashMap<>();
        
        headers.put("messageClass", messageClass);
        
        sendMessageWithAttachments( to, subject, message, attachments, headers );
    }
    
    public void sendMessageWithAttachments( String to, String subject, String message, Map<String, DataHandler> attachments, Map<String, Object> headers ){
        
        headers.put("target", to);
        headers.put("subject", subject);
        
        Exchange exchange = mailProducer.getDefaultEndpoint().createExchange();
        
        Message m = exchange.getIn();
        m.setHeaders(headers);
        m.setBody(message);
        
        for( Map.Entry<String, DataHandler> ent : attachments.entrySet()){
            m.addAttachment(ent.getKey(), ent.getValue());
        }
        
        mailProducer.send(exchange);
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
        
        mailProducer.sendBodyAndHeaders(message, headers);
    }
    
    @Override
    public void sendMessage( Contact contact, String subject, String message, Map<String, Object> params) {
        Map<String, Object> headers = new HashMap<>();
        
        
        headers.put("subject", subject);
        headers.put("messageClass", "GENERIC");
        
        for( Map.Entry<String,Object> e : params.entrySet()){
            headers.put(e.getKey(), e.getValue());
        }
        
        //Gelen parametrelerde e-posta adresi olmaya biliyor.
        headers.put("target", contact.getEmail());
        
        mailProducer.sendBodyAndHeaders(message, headers);
        
    }

    /**
     * Contact'ın e-posta adresi olup olmadığına bakar. 
     * 
     * TODO: E-posta adres doğrulaması yapmak lazım. Logları burada mı basalım.
     * 
     * 
     * 
     * @param contact
     * @return 
     */
    @Override
    public boolean isValidContact(Contact contact) {
        return !Strings.isNullOrEmpty( contact.getEmail());
    }

    @Override
    public void sendMessage(Contact contact, String subject, String message, Map<String, Object> params, Map<String, DataHandler> attachments) {
        sendMessageWithAttachments( contact.getEmail(), subject, message, attachments, params ); 
    }

    
}
