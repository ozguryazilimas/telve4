/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.calendar;

import com.ozguryazilim.telve.auth.UserInfo;
import com.ozguryazilim.telve.calendar.CalendarEventController;
import com.ozguryazilim.telve.calendar.CalendarEventMetadata;
import com.ozguryazilim.telve.calendar.CalendarEventSource;
import com.ozguryazilim.telve.calendar.CalendarFilterModel;
import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 * BPM HumanTask Calendar Event Source Controller.
 * 
 * Üzerinde tarih bulunan human task'ları takvimde göstermek için.
 * 
 * @author Hakan Uygun
 */
@CalendarEventSource( hasDialog = false, creatable = false, dialogPage = Pages.Calendar.SimpleEventDialog.class, styleClass = "tlv-yellow" )
public class TaskEventSource implements CalendarEventController, Serializable{

    @Inject
    private UserInfo userInfo;
    
    @Inject
    private TaskService taskService;
    
    @Inject
    private CalendarFilterModel filterModel;
    
    @Override
    public void createEvent() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(CalendarEventMetadata event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "/bpm/taskConsole.xhtml" + "?faces-redirect=true&task=" + event.getSourceKey());
    }

    @Override
    public void loadEvents(LazyScheduleModel model, Date start, Date end) {
        List<ScheduleEvent> events = getEvents( start, end );
        
        for( ScheduleEvent e : events ){
            model.addEvent(e);
        }
    }

    @Override
    public List<ScheduleEvent> getEvents(Date start, Date end) {
        List<Task> tasks = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(userInfo.getLoginName())
                    .dueBefore(end)
                    .dueAfter(start)
                    .orderByTaskPriority().asc()
                    .orderByDueDate().asc()
                    .listPage(0, 10);
        
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
