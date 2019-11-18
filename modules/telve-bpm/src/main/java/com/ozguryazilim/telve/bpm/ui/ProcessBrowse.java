package com.ozguryazilim.telve.bpm.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.bpm.TaskRepository;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Comment;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camunda Process'leri için Admin tarama ekranı control sınıfı.
 * 
 * Süreç admini olan kişinin süreçleri izlemesini sağlayacak olan kontrol sınıfı.
 * 
 * 
 * @author Hakan Uygun
 */
@WindowScoped
@Named
public class ProcessBrowse implements Serializable{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessBrowse.class); 
    
    @Inject
    private HistoryService historyService;

    @Inject
    private TaskService taskService;
    
    @Inject
    private IdentityService identityService;
    
    @Inject
    RuntimeService runtimeService;
    

    @Inject
    private Identity identity;
    
    @Inject
    private DiagramPopup diagramPopup;
    
    @Inject 
    private TaskRepository taskRepository;
    
    @Inject
    private AuditLogger auditLogger;

    private List<HistoricProcessInstance> processes;
    private List<HistoricTaskInstance> tasks;
    
    private HistoricProcessInstance selectedProcess;
    private HistoricTaskInstance selectedTask;

    private String searchProcessType = "";
    private String searchText = "";
    private List<String> processTypeNames;
    private String processInfo = "";

    
    private List<Comment> comments;
    private String comment;
    private String delegatedUser;
    
    @PostConstruct
    public void init() {
            processTypeNames = ProcessHandlerRegistery.getProcessNames();
    }
    
    /**
     * Süreç üzerinde arama yapar.
     */
    public void search(){
        selectedProcess = null;
        selectedTask = null;
        populateProcesses();
    }
    
    public void populateProcesses(){
        HistoricProcessInstanceQuery qry = historyService.createHistoricProcessInstanceQuery();
        
            if( !Strings.isNullOrEmpty(searchProcessType)){
                qry.processDefinitionKey(searchProcessType);
            }
            
            if( !Strings.isNullOrEmpty(searchText)){
                qry.processInstanceBusinessKeyLike("%"+searchText+"%");
            }
        
            qry.unfinished();
            
                /*.unfinished()
                .finished()
                .startedBy(null)
                .startedAfter(null)
                .startedBefore(null)
                .finishedAfter(null)
                .finishedBefore(null)
                .processDefinitionKey(null)*/
        processes = qry.list();
        
    }
    
    public void populateTasks(){
        if( selectedProcess == null ) return;
        
        tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(selectedProcess.getId())
                .list();
    }
    
    public List<HistoricProcessInstance> getProcesses() {
        return processes;
    }

    public void setProcesses(List<HistoricProcessInstance> processes) {
        this.processes = processes;
    }

    public List<HistoricTaskInstance> getTasks() {
        return tasks;
    }

    public void setTasks(List<HistoricTaskInstance> tasks) {
        this.tasks = tasks;
    }

    public void onProcessSelect(SelectEvent event) {
        selectedProcess = (HistoricProcessInstance) event.getObject();
        populateTasks();
        selectedTask = null;
    }
    
    public void onTaskSelect(SelectEvent event) {
        selectedTask = (HistoricTaskInstance) event.getObject();
    }

    public HistoricProcessInstance getSelectedProcess() {
        return selectedProcess;
    }

    public void setSelectedProcess(HistoricProcessInstance selectedProcess) {
        this.selectedProcess = selectedProcess;
    }

    public String getSearchProcessType() {
        return searchProcessType;
    }

    public void setSearchProcessType(String searchProcessType) {
        this.searchProcessType = searchProcessType;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<String> getProcessTypeNames() {
        return processTypeNames;
    }

    public void setProcessTypeNames(List<String> processTypeNames) {
        this.processTypeNames = processTypeNames;
    }

    public HistoricTaskInstance getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(HistoricTaskInstance selectedTask) {
        this.selectedTask = selectedTask;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDelegatedUser() {
        return delegatedUser;
    }

    public void setDelegatedUser(String delegatedUser) {
        this.delegatedUser = delegatedUser;
    }


    /**
     * İşi başka bir kullanıcının üzerinde atıyalım.
     *
     * Aslında deletegateTask methodu daha doğru olabilir ama bu noktada
     * delegate edilen kullanıcının o iş ikabul etmesi v.b. gibi bir durum da
     * söz konusu...
     *
     */
    public void delegateTask() {
        if (!Strings.isNullOrEmpty(getDelegatedUser())) {
            saveComment();
            taskService.setAssignee(selectedTask.getId(), getDelegatedUser());
            auditLogger.actionLog("ProcessBrowse", 0L, selectedTask.getId(), AuditLogCommand.ACT_UPDATE, AuditLogCommand.ACT_UPDATE, identity.getLoginName(), "Görev başkasına yönlendirildi" );
        }
    }

    /**
     * UI'dan alınan comment'i task'a ekler.
     */
    protected void saveComment() {
        if (!Strings.isNullOrEmpty(comment)) {
            identityService.setAuthenticatedUserId(identity.getLoginName());
            Comment c = taskService.createComment(selectedTask.getId(), selectedTask.getProcessInstanceId(), comment);
        }
        comment = "";
    }
    
    /**
     * Seçili olan taskın başka birine atanıp atanamıyacağının kontrolü.
     * 
     * Eğer task tamamlanmış ise başka birine atanamaz.
     * 
     * @return 
     */
    public boolean disabledDeltegate(){
        if ( selectedTask == null ) return true;
        //Eğer task tamamlanmış ise bir süresi vardır.
        if( selectedTask.getEndTime() == null ) return false;
        return true;
                
    }
    
    /**
     * Task seçili mi?
     * 
     * Başka iş kuralları eklenebilir diye bir metod duzenlendi
     * 
     * @return 
     */
    public boolean disabledKill(){
        LOG.error("Selected Process: " + selectedProcess );
        return ( selectedProcess == null );
    }    
    
    @Transactional
    public Boolean killProcess() {
        try {
            runtimeService.deleteProcessInstance(selectedProcess.getId(), processInfo);
            auditLogger.actionLog("ProcessBrowse", 0L, selectedProcess.getId(), AuditLogCommand.ACT_DELETE, AuditLogCommand.ACT_DELETE, identity.getLoginName(), "Proses sonlandırıldı: " + processInfo );
            return true;
        } catch (BadUserRequestException ex) {
            LOG.error("Process sonlandırılamadı : ".concat(selectedProcess.getId()));
        }
        return false;
    }      
    
    
    /**
     * Seçili olan taskın süreç diagamını gösterecek popup'ı açar.
     */
    public void showDiagram(){
        if( selectedTask == null ) return;
        diagramPopup.openPopup(selectedTask, selectedProcess.getBusinessKey());
    }

    public String getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(String processInfo) {
        this.processInfo = processInfo;
    }
    
     
}
