package com.ozguryazilim.telve.messagebus.command;

import java.io.Serializable;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.ProducerTemplate;
//import org.apache.camel.cdi.ContextName;
//import org.apache.camel.cdi.Uri;

/**
 * Telve MessageBus üzerinden komut göndermek için API.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class CommandSender implements Serializable{
    
    @Inject 
    // FIXME: jakarta: camel 4 ile beraber camel-cdi kalkmış yerine ne kullanacağız?
    //@ContextName("telve")
    //@Uri("seda:command")
    private ProducerTemplate commandMessageProducer; 
    
    
    public void sendCommand( Command command ){
        //Header'a "command" keyi ile gelen komut adı konup gönderiyoruz. Body params'dan oluşuyor.
        commandMessageProducer.sendBodyAndHeader(command, "command", command.getClass().getName());
    }
}
