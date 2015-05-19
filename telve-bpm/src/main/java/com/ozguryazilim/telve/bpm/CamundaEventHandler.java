/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import com.ozguryazilim.telve.channel.notify.NotifyChannel;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.camunda.bpm.engine.cdi.BusinessProcessEvent;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BPM Engine içinde gerçekleşen eventleri observ edecek
 * @author Hakan Uygun
 */
@Dependent
public class CamundaEventHandler implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(CamundaEventHandler.class);
    
    @Inject
    private NotifyChannel notifyChannel;
    
    public void onTaskEvent(@Observes BusinessProcessEvent businessProcessEvent) {
        LOG.debug("on bpm event {}", businessProcessEvent);
        
        if( businessProcessEvent.getType().getTypeName().equals(TaskListener.EVENTNAME_ASSIGNMENT) ){
           notifyChannel.sendMessage(businessProcessEvent.getTask().getAssignee(), "messages.task.Assignment", businessProcessEvent.getTask().getName());
        }
    }
    
}
