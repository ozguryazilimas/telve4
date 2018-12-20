package com.ozguryazilim.telve.notify;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

/**
 * MessageBus üzerinden notification messajı göndermek için API
 * @author Hakan Uygun
 */
@Named
@Dependent
public class NotifySender implements Serializable{
    
    @Inject @ContextName("telve")
    @Uri("seda:notify")
    private ProducerTemplate notifyProducer; 
    
    public void sendNotify( String message ){
        NotifyMessage nm = new NotifyMessage();
        nm.setSubject(message);
        nm.setBody(message);
        sendNotify(nm);
    }
    
    public void sendNotify( String subject, String message ){
        NotifyMessage nm = new NotifyMessage( subject, message );
        sendNotify(nm);
    }
    
    public void sendNotify( String to, String subject, String message ){
        NotifyMessage nm = new NotifyMessage( to, subject, message );
        sendNotify(nm);
    }
    
    public void sendNotify( NotifyMessage message ){
        notifyProducer.sendBody(message);
    }
}
