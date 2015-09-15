/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.notification.NotificationCommand;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.camunda.bpm.engine.cdi.BusinessProcessEvent;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                sendNotification( businessProcessEvent.getTask().getAssignee(), businessProcessEvent.getTask(), "Assignment" );
            }

        } else if (businessProcessEvent.getType().getTypeName().equals(TaskListener.EVENTNAME_CREATE)) {
            for (IdentityLink il : businessProcessEvent.getTask().getCandidates()) {
                //TODO: Grup atamalarında buranın düzenlenmesi lazım.
                sendNotification( il.getUserId(), businessProcessEvent.getTask(), "CandidateAssignment" );
            }
        }
    }

    protected void sendNotification(String userId, DelegateTask task, String template ) {
        NotificationCommand nc = new NotificationCommand();

        nc.setNotificationClass("BPMTaskAssignment");
        nc.setSender("SYSTEM");
        nc.setSubject("messages.task.Assignment");
        nc.setTarget("cs=user;username=" + userId );
        nc.setTemplate(template);
        Map<String, Object> params = new HashMap<>();

        params.put("TaskName", task.getName());
        params.put("TaskId", task.getId());

        nc.setParams(params);

        commandSender.sendCommand(nc);
    }

}
