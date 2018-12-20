package com.ozguryazilim.telve.bpm.dashlets;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.dashboard.AbstractDashlet;
import com.ozguryazilim.telve.dashboard.Dashlet;
import com.ozguryazilim.telve.dashboard.DashletCapability;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

/**
 * Kişinin aktif görevlerini listeleyen dashlet
 * 
 * @author Hakan Uygun
 */
@Dashlet(capability = {DashletCapability.canHide, DashletCapability.canMinimize, DashletCapability.canRefresh})
public class TaskDashlet extends AbstractDashlet{

    @Inject
    private Identity identity;
    
    @Inject
    private TaskService taskService;
    
    private List<Task> tasks;
    private List<Task> dueTasks;
    private List<Task> followupTasks;
    
    
    public List<Task> getTasks(){
        if( tasks == null ){
            tasks = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getLoginName())
                    .orderByTaskPriority().asc()
                    .orderByFollowUpDate().asc()
                    .orderByDueDate().asc()
                    .listPage(0, 10);
        }
        
        return tasks;
    }

    public List<Task> getDueTasks() {
        if( dueTasks == null ){
            dueTasks = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getLoginName())
                    .dueBefore(new Date())
                    .orderByTaskPriority().asc()
                    .orderByDueDate().asc()
                    .listPage(0, 10);
        }
        return dueTasks;
    }

    public List<Task> getFollowupTasks() {
        if( followupTasks == null ){
            followupTasks = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getLoginName())
                    .followUpBefore(new Date())
                    .orderByTaskPriority().asc()
                    .orderByFollowUpDate().asc()
                    .listPage(0, 10);
        }
        return followupTasks;
    }

    @Override
    public void refresh() {
        followupTasks = null;
        dueTasks = null;
        tasks = null;
        super.refresh(); 
    }
    
    
}
