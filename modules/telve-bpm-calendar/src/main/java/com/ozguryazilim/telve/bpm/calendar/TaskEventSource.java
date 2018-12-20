package com.ozguryazilim.telve.bpm.calendar;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.calendar.AbstractCalendarEventSource;
import com.ozguryazilim.telve.calendar.CalendarEventModel;
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

/**
 * BPM HumanTask Calendar Event Source Controller.
 * 
 * Üzerinde tarih bulunan human task'ları takvimde göstermek için.
 * 
 * 
 * @author Hakan Uygun
 */
@CalendarEventSource( hasDialog = false, creatable = false, permission = "NEED_LOGIN" )
public class TaskEventSource extends AbstractCalendarEventSource{

    @Inject
    private Identity identity;
    
    @Inject
    private TaskService taskService;
    
    @Inject
    private CalendarFilterModel filterModel;
    
    @Override
    public void process(String eventId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "/bpm/taskConsole.xhtml" + "?faces-redirect=true&task=" + eventId);
    }


    @Override
    public List<CalendarEventModel> getEvents(Date start, Date end) {
        List<Task> tasks = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getLoginName())
                    .dueBefore(end)
                    .dueAfter(start)
                    .orderByTaskPriority().asc()
                    .orderByDueDate().asc()
                    .listPage(0, 50);
        
        List<CalendarEventModel> ls = new ArrayList<>();
        for( Task t : tasks ){
            ls.add(getCalendarEventModel(t));
        }
        return ls;
    }
    
    /**
     * Verilen CalendarEvent modelinden DefaultScheduleEvent üretir.
     * @param event
     * @return 
     */
    protected CalendarEventModel getCalendarEventModel( Task task ){
        CalendarEventModel e = new CalendarEventModel();
        
        String subject = (String) taskService.getVariable(task.getId(), "SUBJECT");
        
        e.setId(task.getId());
        e.setTitle( subject );
        e.setDescription(task.getDescription());
        e.setAllDay(true);
        e.setEditable(false);
        //e.setStartDate(task.getFollowUpDate());
        e.setStart(task.getDueDate());
        e.setEnd(task.getDueDate());
        
        return e;
    }
    
}
