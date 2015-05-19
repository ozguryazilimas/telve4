/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.bpm.handlers.AbstractDialogProcessHandler;
import com.ozguryazilim.telve.bpm.handlers.AbstractHumanTaskHandler;
import com.ozguryazilim.telve.bpm.handlers.HumanTaskHandlerRegistery;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.picketlink.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcıya ait task listesini sunan konsol
 *
 * @author Hakan Uygun
 */
@Named
@WindowScoped
public class TaskConsole implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TaskConsole.class);

    @Inject
    private Identity identity;

    @Inject
    private IdentityService identityService;

    @Inject
    private TaskService taskService;

    private List<Task> taskList;
    private Task selectedTask;
    private String selectedTaskViewId = "/bpm/emptyTask.xhtml";

    private String searchText = "";
    private String searchTaskType = "";

    private List<String> taskTypeNames;

    @PostConstruct
    public void init() {
        taskTypeNames = HumanTaskHandlerRegistery.getTaskNames();
        //TODO: camnuda için hangi kullanıcı çalışıyor setliyoruz. Aslında bunu login tarafına felan almak lazım sanırım.
        identityService.setAuthenticatedUserId(identity.getAccount().getId());
    }

    /**
     * Sistemde tanımlı UI üzerinden başlatılabilecek process handler listesini
     * döndürür.
     *
     * TODO: Burada hak kontrolü de yapmak lazım sanırım.
     *
     * @return
     */
    public List<String> getProcessLists() {
        return ProcessHandlerRegistery.getStartableProcessHandlerNames();
    }

    /**
     * Geriye ismi verilen process için tanımlı ikon yolunu döndürür.
     *
     * @param name
     * @return
     */
    public String getIconPath(String name) {
        return ProcessHandlerRegistery.getIconPath(name);
    }

    /**
     * Adı verilen task için fa icon döndürür.
     *
     * @param taskName
     * @return
     */
    public String getTaskIcon(String taskName) {
        return HumanTaskHandlerRegistery.getIconName(taskName);
    }

    /**
     * İsmi verilen task için popup diloğunu açar
     *
     * @param task
     */
    public void openTaskDialog(Task task) {
        String hn = HumanTaskHandlerRegistery.getHandlerName(task.getTaskDefinitionKey());
        AbstractHumanTaskHandler aph = (AbstractHumanTaskHandler) BeanProvider.getContextualReference(hn, true);
        aph.openDialog(task);
    }

    /**
     * Verilen isme sahip processHandler'ı bulup startFormu'nu çağırır.
     *
     * @param name
     */
    public void startProcess(String name) {
        LOG.info("Process to start {}", name);
        AbstractDialogProcessHandler aph = (AbstractDialogProcessHandler) BeanProvider.getContextualReference(name, true);
        aph.openDialog();
    }

    public List<Task> getTaskList() {
        if (taskList == null) {
            TaskQuery qry = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getAccount().getId())
                    .orderByDueDate().asc()
                    .orderByTaskName().asc();

            if (!Strings.isNullOrEmpty(searchText)) {
                qry.taskNameLike("%" + searchText + "%");
            }

            if (!Strings.isNullOrEmpty(searchTaskType)) {
                qry.taskDefinitionKey(searchTaskType);
            }

            taskList = qry.list();
        }

        return taskList;
    }

    /**
     * Mevcut task listesini yeniler.
     */
    public void refresh() {
        taskList = null;
        selectedTask = null;
        selectedTaskViewId = "/bpm/emptyTask.xhtml";
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public String getSelectedTaskViewId() {
        return selectedTaskViewId;
    }

    public void setSelectedTaskViewId(String selectedTaskViewId) {
        this.selectedTaskViewId = selectedTaskViewId;
    }

    public void onSelectTask(Task selectedTask) {
        setSelectedTask(selectedTask);
        String hn = HumanTaskHandlerRegistery.getHandlerName(getSelectedTask().getTaskDefinitionKey());
        AbstractHumanTaskHandler aph = (AbstractHumanTaskHandler) BeanProvider.getContextualReference(hn, true);
        aph.setTask(selectedTask);
        setSelectedTaskViewId(aph.getViewId());
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchTaskType() {
        return searchTaskType;
    }

    public void setSearchTaskType(String searchTaskType) {
        this.searchTaskType = searchTaskType;
    }

    public List<String> getTaskTypeNames() {
        return taskTypeNames;
    }

    /**
     * URL üzerinden task seçilebilmesi için
     *
     * @param taskId
     */
    public void setSelectedTaskId(String taskId) {
        if (!Strings.isNullOrEmpty(taskId)) {
            List<Task> ls = taskService.createTaskQuery()
                    .active()
                    .taskAssignee(identity.getAccount().getId())
                    .taskId(taskId)
                    .list();

            if (!ls.isEmpty()) {
                onSelectTask(ls.get(0));
            }
        }
    }
    
    public String getSelectedTaskId(){
        return "";
    }

}
