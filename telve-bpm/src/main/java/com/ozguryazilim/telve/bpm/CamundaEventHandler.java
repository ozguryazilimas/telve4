/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.camunda.bpm.engine.cdi.BusinessProcessEvent;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.notification.NotificationCommand;

/**
 * BPM Engine içinde gerçekleşen eventleri observ edecek
 *
 * @author Hakan Uygun
 */
@Dependent
public class CamundaEventHandler implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CamundaEventHandler.class);

    @Inject
    private CommandSender commandSender;

    public void onTaskEvent(@Observes BusinessProcessEvent businessProcessEvent) {
        LOG.debug("on bpm event {}", businessProcessEvent);

        if (businessProcessEvent.getType().getTypeName().equals(TaskListener.EVENTNAME_ASSIGNMENT)) {

            if (!Strings.isNullOrEmpty(businessProcessEvent.getTask().getAssignee())) {
                Boolean b = (Boolean) businessProcessEvent.getTask().getVariableLocal("NOTIFICATION");
                if( b == null || b ){
                    sendNotification( businessProcessEvent.getTask().getAssignee(), businessProcessEvent.getTask(), businessProcessEvent.getProcessDefinition(), "Assignment" );
                }
            }

        } else if (businessProcessEvent.getType().getTypeName().equals(TaskListener.EVENTNAME_CREATE)) {
            for (IdentityLink il : businessProcessEvent.getTask().getCandidates()) {
                Boolean b = (Boolean) businessProcessEvent.getTask().getVariable("CANDIDATE_NOTIFICATION");
                if( ( b == null || b ) && !Strings.isNullOrEmpty( il.getUserId() ) && !il.getUserId().equals( businessProcessEvent.getTask().getAssignee())){
                    //TODO: Grup atamalarında buranın düzenlenmesi lazım.
                    sendNotification( il.getUserId(), businessProcessEvent.getTask(), businessProcessEvent.getProcessDefinition(), "CandidateAssignment" );
                }
            }
        }
    }

    protected void sendNotification(String userId, DelegateTask task, ProcessDefinition processDefinition, String template ) {
        NotificationCommand nc = new NotificationCommand();

        nc.setNotificationClass("BPMTaskAssignment");
        nc.setSender("SYSTEM");
        nc.setSubject("messages.task.Assignment");
        nc.setTarget("cs=user;username=" + userId );
        nc.setTemplate(template);
        Map<String, Object> params = new HashMap<>();

        //Öncelikle süreçten gelen herşeyi bir koyalım
        params.putAll( task.getVariables());

        params.put("TaskName", task.getName());
        params.put("TaskId", task.getId());

        params.put("TaskKey", task.getTaskDefinitionKey());
        params.put("TaskPriority", task.getPriority());
        params.put("TaskAssignee", task.getAssignee());
        params.put("TaskDueDate", task.getDueDate());
        params.put("TaskInfo", task.getDescription());
        params.put("TaskProcess", processDefinition.getKey());
        params.put("TaskSubject", task.getVariable("SUBJECT"));

        nc.setParams(params);



        commandSender.sendCommand(nc);
    }

}
