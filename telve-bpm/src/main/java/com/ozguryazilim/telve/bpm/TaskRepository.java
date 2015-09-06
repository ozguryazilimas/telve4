/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.UserLookup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;

/**
 * Camunda Task servislerini kullanarak telve içinde daha kolay kullanımlı bir
 * API verir.
 *
 * @author Hakan Uygun
 */
@Dependent
public class TaskRepository implements Serializable {

    @Inject
    private UserLookup userLookup;

    @Inject
    private TaskService taskService;

    
    
    /**
     * Verilen parametrelere göre task listesini süzüp döndürür.
     * 
     * 
     * @param userId sorgu yapılacak kullanıcı id
     * @param assigned üzerine atanmış işler gelsin
     * @param candidate aday işler gelsin
     * @param processKey verilen key'e sahip processler için task listesi eğer verilmez ise dikkate alınmaz
     * @param taskKey verilen key'e sahip task listesi eğer verilmez ise dikkate alınmaz
     * @param filter verilen değeri içeren subject'lere sahip tasklar verilmez ise dikkate alınmaz
     * @return 
     */
    public List<TaskInfo> getTaskList( String userId, Boolean assigned, Boolean candidate, String processKey, String taskKey, String filter ) {

        //Önce bir task listesini alalım.
        TaskQuery qry = taskService.createTaskQuery()
                .initializeFormKeys() // must be invoked
                .active()
                .orderByDueDate().asc()
                .orderByTaskName().asc();

        //Atanma kontrolü bakalım.
        if( assigned && !candidate ){
            qry.taskAssignee(userId);
        } else if ( !assigned && candidate ){
            qry.taskCandidateUser(userId);
        } else {
            qry.taskCandidateUser(userId)
               .includeAssignedTasks();
        }
        
        //Verilen değeri SUBJECT içerir mi diye bakıyoruz
        if (!Strings.isNullOrEmpty(filter)) {
            qry.processVariableValueLike("SUBJECT", "%" + filter + "%");
        }

        //Task tipi verilmiş mi o zaman sadece onları alalım
        if (!Strings.isNullOrEmpty(taskKey)) {
            qry.taskDefinitionKey(taskKey);
        }

        //Process tipi verilmiş mi o zaman sadece onları alalım 
        if (!Strings.isNullOrEmpty(processKey)) {
            qry.processDefinitionKey(processKey);
        }

        List<Task> taskList = qry.list();

        return buildTaskInfos(taskList);
    }

    /**
     * Verilen Task ID'si için geriye task info döndürür.
     * @param taskId
     * @return 
     */
    public TaskInfo getTaskById( String taskId ){
        if (!Strings.isNullOrEmpty(taskId)) {
            List<Task> ls = taskService.createTaskQuery()
                    .initializeFormKeys() // must be invoked
                    .active()
                    //.taskAssignee(identity.getAccount().getId())
                    .taskId(taskId)
                    .list();

            if (!ls.isEmpty()) {
                return  new TaskInfo( ls.get(0), taskService.getVariables(ls.get(0).getId()));
            }
        }
        
        return null;
    }
    
    /**
     * Verilen task listesinden task info listesi oluşturur.
     *
     * @param tasks
     * @return
     */
    public List<TaskInfo> buildTaskInfos(List<Task> tasks) {
        List<TaskInfo> result = new ArrayList<>();
        //variable dongusu da yaparak TaskInfo oluşturalım.
        for (Task task : tasks) {
            result.add(new TaskInfo(task, taskService.getVariables(task.getId())));
        }
        return result;
    }
}
