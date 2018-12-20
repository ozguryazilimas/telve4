package com.ozguryazilim.telve.bpm.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.bpm.TaskInfo;
import com.ozguryazilim.telve.bpm.TaskRepository;
import com.ozguryazilim.telve.bpm.handlers.AbstractDialogProcessHandler;
import com.ozguryazilim.telve.bpm.handlers.AbstractHumanTaskHandler;
import com.ozguryazilim.telve.bpm.handlers.HumanTaskHandlerRegistery;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
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
    
    @Inject 
    private TaskRepository taskRepository;
            

    @Inject
    private DiagramPopup diagramPopup;
    
    private List<TaskInfo> taskList;
    private TaskInfo selectedTask;
    private String selectedTaskViewId = "/bpm/emptyTask.xhtml";

    private String searchText = "";
    private String searchTaskType = "";
    private String searchProcessType = "";
    private Boolean myTasks = Boolean.TRUE;
    private Boolean potTasks = Boolean.TRUE;
    private String taskOwnerType = "myTasks";
    
    private List<String> taskTypeNames;
    private List<String> processTypeNames;

    @PostConstruct
    public void init() {
        taskTypeNames = HumanTaskHandlerRegistery.getTaskNames();
        processTypeNames = ProcessHandlerRegistery.getProcessNames();
        //TODO: camnuda için hangi kullanıcı çalışıyor setliyoruz. Aslında bunu login tarafına felan almak lazım sanırım.
        identityService.setAuthenticatedUserId(identity.getLoginName());
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
    public void openTaskDialog(TaskInfo task) {
        String hn = HumanTaskHandlerRegistery.getHandlerName(task.getFormKey());
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

    public List<TaskInfo> getTaskList() {
        if (taskList == null) {
            taskList = taskRepository.getTaskList(identity.getLoginName(), "myTasks".equals(getTaskOwnerType()), "potTasks".equals(getTaskOwnerType()), searchProcessType, searchTaskType, searchText);
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
        
        //TaskList'e bulunan ilk item'ı seçer.
        /* Kullanıcıların kafası karıştığına dair şikayet geldi. Boş ekrana düşecek.
        List<TaskInfo> ls = getTaskList();
        if( !ls.isEmpty()){
            onSelectTask( ls.get(0));
        }*/
    }

    public TaskInfo getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(TaskInfo selectedTask) {
        this.selectedTask = selectedTask;
    }

    public String getSelectedTaskViewId() {
        return selectedTaskViewId;
    }

    public void setSelectedTaskViewId(String selectedTaskViewId) {
        this.selectedTaskViewId = selectedTaskViewId;
    }

    /**
     * UI tarafından seçilen task'ın handlerı bulunup hazırlanıyor.
     * @param selectedTask 
     */
    public void onSelectTask(TaskInfo selectedTask) {
        setSelectedTask(selectedTask);
        String hn = HumanTaskHandlerRegistery.getHandlerName(getSelectedTask().getFormKey());
        if( Strings.isNullOrEmpty(hn)){
            //TODO: i18n
            FacesMessages.error("Task Form Bulunamadı", "Task " + getSelectedTask().getTask().getTaskDefinitionKey() + " için tanımlı bir form bulunamadı. Lütfen sistem yöenticiniz ile görüşünüz.");
            LOG.error("Task {} için tanımlı handler bulunamadı", getSelectedTask().getTask().getTaskDefinitionKey());
            return;
        }
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
            TaskInfo ti = taskRepository.getTaskById(taskId);
            
            if ( ti != null ) {
                onSelectTask(ti);
            }
        }
    }
    
    public String getSelectedTaskId(){
        return "";
    }

    public Boolean getMyTasks() {
        return myTasks;
    }

    public void setMyTasks(Boolean myTasks) {
        this.myTasks = myTasks;
    }

    public Boolean getPotTasks() {
        return potTasks;
    }

    public void setPotTasks(Boolean potTasks) {
        this.potTasks = potTasks;
    }

    public String getTaskOwnerType() {
        return taskOwnerType;
    }

    public void setTaskOwnerType(String taskOwnerType) {
        this.taskOwnerType = taskOwnerType;
    }

    public String getSearchProcessType() {
        return searchProcessType;
    }

    public void setSearchProcessType(String searchProcessType) {
        this.searchProcessType = searchProcessType;
    }

    public List<String> getProcessTypeNames() {
        return processTypeNames;
    }

    /**
     * Seçili olan taskın süreç diagamını gösterecek popup'ı açar.
     */
    public void showDiagram(){
        diagramPopup.openPopup(selectedTask);
    }
    
    public void setTaskListType( String typ ){
        taskOwnerType = typ;
        refresh();
    }
    
    public void showMyTasks(){
        setTaskListType( "myTasks" );
    }
    
    public void showPotTasks(){
        setTaskListType( "potTasks" );
    }
    
    public Integer getMyTaskCount(){
        return taskRepository.getTaskCount(identity.getLoginName(), true );
    }
    
    public Integer getPotTaskCount(){
        return taskRepository.getTaskCount(identity.getLoginName(), false );
    }
}
