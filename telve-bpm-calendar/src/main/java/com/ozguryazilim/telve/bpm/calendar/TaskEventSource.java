/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.calendar;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.calendar.AbstractCalendarEventSource;
import com.ozguryazilim.telve.calendar.CalendarEventMetadata;
import com.ozguryazilim.telve.calendar.CalendarFilterModel;
import com.ozguryazilim.telve.calendar.annotations.CalendarEventSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

/**
 * BPM HumanTask Calendar Event Source Controller.
 * 
 * Üzerinde tarih bulunan human task'ları takvimde göstermek için.
 * 
 * 
 * @author Hakan Uygun
 */
@CalendarEventSource( hasDialog = false, creatable = false )
public class TaskEventSource extends AbstractCalendarEventSource{

    @Inject
    private Identity identity;
    
    @Inject
    private TaskService taskService;
    
    @Inject
    private CalendarFilterModel filterModel;
    
    @Override
    public void process(CalendarEventMetadata event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "/bpm/taskConsole.xhtml" + "?faces-redirect=true&task=" + event.getSourceKey());
    }


    @Override
    public List<ScheduleEvent> getEvents(Date start, Date end) {
        List<Task> tasks = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getLoginName())
                    .dueBefore(end)
                    .dueAfter(start)
                    .orderByTaskPriority().asc()
                    .orderByDueDate().asc()
                    .listPage(0, 50);
        
        List<ScheduleEvent> ls = new ArrayList<>();
        for( Task t : tasks ){
            ls.add(getScheduleEvent(t));
        }
        return ls;
    }
    
    /**
     * Verilen CalendarEvent modelinden DefaultScheduleEvent üretir.
     * @param event
     * @return 
     */
    protected DefaultScheduleEvent getScheduleEvent( Task task ){
        DefaultScheduleEvent e = new DefaultScheduleEvent();
        
        String subject = (String) taskService.getVariable(task.getId(), "SUBJECT");
        
        e.setTitle( subject );
        e.setDescription(task.getDescription());
        e.setAllDay(true);
        e.setEditable(false);
        //e.setStartDate(task.getFollowUpDate());
        e.setStartDate(task.getDueDate());
        e.setEndDate(task.getDueDate());
        
        e.setStyleClass( filterModel.getSourceStyle("taskEventSource"));
        CalendarEventMetadata meta = new CalendarEventMetadata();
        meta.setSourceKey(task.getId());
        meta.setSourceName("taskEventSource");
        e.setData(meta);
        
        
        return e;
    }
    
}
