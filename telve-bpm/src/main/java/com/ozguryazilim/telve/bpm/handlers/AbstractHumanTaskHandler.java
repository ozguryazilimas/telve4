/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.bpm.ui.TaskConsole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.picketlink.Identity;
import org.primefaces.context.RequestContext;

/**
 * Sistemde tanımlı Human Task handlerlar için taban sınıf.
 * 
 * BPM üzerinde tanımlı her HumanTask için bir handler'a ihtiyaç bulunuyor.
 * 
 * Bu handlerlar, kullanıcı etkileşimi için gerekli UI kurallarını işletmekten sorumlular.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractHumanTaskHandler implements Serializable{
 
    @Inject
    private TaskService taskService;
    
    @Inject
    private TaskConsole taskConsole;
    
    @Inject 
    private Identity identity;
    
    @Inject 
    private IdentityService identityService;
    
    private Task task;
    private List<TaskResultCommand> resultCommands = new ArrayList<>();
    private List<Comment> comments;
    private String comment;
    
    public void openDialog( Task task ) {
        
        this.task = task;
        this.comment = "";
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        popVariables(taskService.getVariables(task.getId()));
        
        resultCommands.clear();
        initResultButtons();
        
        comments = taskService.getTaskComments(task.getId());
        
        RequestContext.getCurrentInstance().openDialog( getDialogName(), options, null);
    }
    
    /**
     * Task'ı verilen sonucu ekleyerek kapatır.
     * 
     * @param result 
     */
    public void closeTask( String result) {
        saveComment();
        
        Map<String, Object> values = new HashMap<>();
        pushVariables(values);
        values.put("RESULT", result);
        taskService.complete(task.getId(), values);
        taskConsole.refresh();
        onAfterClose();
        //RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    /**
     * Task kapatıldıktan sonra çağrılır. 
     * Task handlerlar bu noktada orjinal kaydı düzenleyebilir.
     */
    protected void onAfterClose(){
        //Alt sınıflar override etsin diye var.
    }
    
    /**
     * UI üzerinden toplanan verileri saklar.
     */
    public void save() {
        saveComment();
        
        Map<String, Object> values = new HashMap<>();
        pushVariables(values);
        taskService.setVariables(task.getId(), values);
        
        comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
    }
    
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    public List<TaskResultCommand> getResultCommands() {
        return resultCommands;
    }

    public void setResultCommands(List<TaskResultCommand> resultCommands) {
        this.resultCommands = resultCommands;
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

    
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        if( !task.equals(this.task)){
            this.task = task;
            
            this.comment = "";
            popVariables(taskService.getVariables(task.getId()));
            resultCommands.clear();
            initResultButtons();
        
            comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
        }
    }
    
    /**
     * UI'dan alınan comment'i task'a ekler.
     */
    protected void saveComment(){
        if( !Strings.isNullOrEmpty(comment) ){
            identityService.setAuthenticatedUserId(identity.getAccount().getId());
            Comment c = taskService.createComment(task.getId(), task.getProcessInstanceId(), comment);
        }
        comment = "";
    }
    
    public abstract String getDialogName();
    
    /**
     * Task yükleme sonrası task variablelarınıokumak için
     * @param variables 
     */
    protected abstract void popVariables( Map<String, Object> variables );
    
    /**
     * Task bitiminde task sonuç variablelarını geri basmak için
     * @param variables 
     */
    protected abstract void pushVariables( Map<String, Object> variables );

    /**
     * Dailog üzerinde çıkacak olan kapatma düğmelerini ayarlar.
     * 
     * Farklı türden sonuçlar dönecekse bu method override edilmeli.
     */
    protected void initResultButtons() {
        resultCommands.add(TaskResultCommand.COMPLETE);
    }
    
    /**
     * Geriye TaskConsole üzerinde gösterilecek olan view'ın ID'sini döndürür.
     * Bu view /layout/taskBase.xhtml üretilmiş olmalıdır.
     * 
     * FIXME: Bu bilgiyi aslında annotation'dan almak daha iyi olacak.
     * 
     * @return 
     */
    public abstract String getViewId();
    
    protected TaskService getTaskService(){
        return taskService;
    }

}
