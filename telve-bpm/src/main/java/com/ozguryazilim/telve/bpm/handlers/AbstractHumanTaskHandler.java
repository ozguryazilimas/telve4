/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.bpm.TaskInfo;
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
    
    private TaskInfo task;
    private List<TaskResultCommand> resultCommands = new ArrayList<>();
    private List<Comment> comments;
    private String comment;
    
    public void openDialog( TaskInfo task ) {
        
        this.task = task;
        this.comment = "";
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        popVariables();
        
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
        
        onBeforeClose( result );
        
        saveComment();
        
        pushVariables();
        
        task.getVariables().put("RESULT", result);
        taskService.complete(task.getId(), task.getVariables());
        taskConsole.refresh();
        onAfterClose( result );
        //RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    /**
     * Task kapatıldıktan sonra çağrılır. 
     * Task handlerlar bu noktada orjinal kaydı düzenleyebilir.
     * @param result kapanış değerinin ne olduğu
     */
    protected void onAfterClose( String result ){
        //Alt sınıflar override etsin diye var.
    }
    
    /**
     * Task kapanmadan önce çağrılır. 
     * Task handlerlar bu noktada orjinal kaydı düzenleyebilir.
     * @param result kapanış değerinin ne olduğu
     */
    protected void onBeforeClose( String result ){
        //Alt sınıflar override etsin diye var.
    }
    
    /**
     * UI üzerinden toplanan verileri saklar.
     */
    public void save() {
        onBeforeSave();
        
        saveComment();
        
        pushVariables();
        
        taskService.setVariables(task.getId(), task.getVariables());
        
        comments = taskService.getProcessInstanceComments(task.getTask().getProcessInstanceId());
        
        onAfterSave();
    }
    
    /**
     * Task değerleri save edilmeden hemen önce çağrılır.
     */
    protected void onBeforeSave(){
        
    }
    
    /**
     * Task değerleri save edildikten hemen sonra çağrılır.
     */
    protected void onAfterSave(){
        
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

    
    public TaskInfo getTask() {
        return task;
    }

    public void setTask(TaskInfo task) {
        if( !task.equals(this.task)){
            this.task = task;
            
            this.comment = "";
            popVariables();
            resultCommands.clear();
            initResultButtons();
        
            comments = taskService.getProcessInstanceComments(task.getTask().getProcessInstanceId());
        }
    }
    
    /**
     * UI'dan alınan comment'i task'a ekler.
     */
    protected void saveComment(){
        if( !Strings.isNullOrEmpty(comment) ){
            //FIXME: userID işini ne yapacağız?
            identityService.setAuthenticatedUserId(identity.getAccount().getId());
            Comment c = taskService.createComment(task.getId(), task.getTask().getProcessInstanceId(), comment);
        }
        comment = "";
    }
    
    public abstract String getDialogName();
    
    /**
     * Task yükleme sonrası task variablelarını okumak için.
     * 
     * Tüm degerler task.getVariables() ile alınabilir ama bu esnada bir değerin alınıp işlenmesi için kulanılır.
     * 
     */
    protected void popVariables(){
        
    }
    
    /**
     * Task bitiminde task sonuç variablelarını geri basmak için.
     * 
     * save'den hemen önce process engine bir değer basmak isteniyor ise kullanılır.
     */
    protected void pushVariables(){
        
    }

    /**
     * Dailog üzerinde çıkacak olan kapatma düğmelerini ayarlar.
     * 
     * Farklı türden sonuçlar dönecekse bu method override edilmeli.
     */
    protected void initResultButtons() {
        
        String s = task.getAcceptableResults();
        
        //Değer yoksa varsayılan COMPLETE
        if( Strings.isNullOrEmpty(s)){
            resultCommands.add(TaskResultCommand.COMPLETE);
            return;
        }

        List<String> ls = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(s);
        
        //Şimdi her komut karşılığı bulunup düğme oluşturulacak
        for( String cmd : ls ){
            resultCommands.add(TaskResultCommand.getCommand(cmd));
        }
        
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
