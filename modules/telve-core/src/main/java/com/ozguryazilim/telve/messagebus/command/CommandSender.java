/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

/**
 * Telve MessageBus üzerinden komut göndermek için API.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class CommandSender implements Serializable{
    
    @Inject @ContextName("telve")
    @Uri("seda:command")
    private ProducerTemplate commandMessageProducer; 
    
    
    public void sendCommand( Command command ){
        //Header'a "command" keyi ile gelen komut adı konup gönderiyoruz. Body params'dan oluşuyor.
        commandMessageProducer.sendBodyAndHeader(command, "command", command.getClass().getName());
    }
}
