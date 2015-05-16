/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.email;

import com.ozguryazilim.telve.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
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
public class EmailChannel implements Channel{
    
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
}
