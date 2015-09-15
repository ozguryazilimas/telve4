/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.notification.NotificationCommand;
import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.cdi.annotation.ExecutionId;

/**
 * BPM süreçleri içerisinden mesaj göndermek için NotifyChannel köprüsü.
 * 
 * BPM Process değişkenlerinin tamamını NotificationCommand içerisine yerleştirir.
 * Şablonun bu değerleri kullanması gerekir.
 * 
 * Uyarının gideceği target bilgisinin BPM tarafında gerekirse bir betik ile hazırlanması gerekir.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class MessageSendTaskHandler implements Serializable{
   
    @Inject
    private RuntimeService runtimeService;
    
    @Inject @ExecutionId 
    private String currentExecutionId;
    
    @Inject
    private CommandSender commandSender;
    
    public void sendMessage( String target, String template  ){
        
        Map<String, Object> variables = runtimeService.getVariables(currentExecutionId);
    
        NotificationCommand rm = new NotificationCommand();
        
        rm.setNotificationClass("BPMMessageSender");
        rm.setSender("SYSTEM");
        rm.setSubject((String) variables.get("SUBJECT"));
        rm.setTarget(target);
        rm.setTemplate(template);
        rm.setParams(variables);
        
        commandSender.sendCommand(rm);
        
    }
    
}
