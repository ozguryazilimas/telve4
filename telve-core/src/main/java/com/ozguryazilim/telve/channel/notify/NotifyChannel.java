/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.notify;

import com.ozguryazilim.telve.channel.Channel;
import com.ozguryazilim.telve.contact.Contact;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

/**
 * NotifyChannel üzerinden mesaj göndermek için kullanılır.
 * @author Hakan Uygun
 */
@Dependent
@Named
public class NotifyChannel implements Channel{

    @Inject @ContextName("telve")
    @Uri("seda:notifyChannel")
    private ProducerTemplate notifyProducer; 
    
    
    @Override
    public void sendMessage( String to, String subject, String message ){
        Map<String, Object> headers = new HashMap<>();
        
        headers.put("target", to);
        headers.put("subject", subject);
        headers.put("messageClass", "GENERIC");
        
        notifyProducer.sendBodyAndHeaders(message, headers);
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
        
        notifyProducer.sendBodyAndHeaders(message, headers);
    }

    /**
     * Contact'ın tipinin kullanıcı olup olmadığına bakar.
     * 
     * TODO: UserType ile isim uzayı oluşturulur ise bu kontrolün düzenlenmesi gerek.
     * 
     * @param contact
     * @return 
     */
    @Override
    public boolean isValidContact(Contact contact) {
        return "User".equals(contact.getType());
    }

    @Override
    public void sendMessage(Contact contact, String subject, String message, Map<String, Object> params) {
        Map<String, Object> headers = new HashMap<>();
        
        
        headers.put("subject", subject);
        headers.put("messageClass", "GENERIC");
        
        for( Map.Entry<String,Object> e : params.entrySet()){
            headers.put(e.getKey(), e.getValue());
        }
        
        //Kullanıcı user id'sine gidecek
        headers.put("target", contact.getId());
        
        notifyProducer.sendBodyAndHeaders(message, headers);
    }
}
