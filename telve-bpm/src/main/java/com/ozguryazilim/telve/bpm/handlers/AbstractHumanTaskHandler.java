/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.bpm.TaskInfo;
import com.ozguryazilim.telve.bpm.TaskRepository;
import com.ozguryazilim.telve.bpm.ui.TaskConsole;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.DialogBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.deltaspike.core.util.ProxyUtils;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskAlreadyClaimedException;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Comment;

/**
 * Sistemde tanımlı Human Task handlerlar için taban sınıf.
 *
 * BPM üzerinde tanımlı her HumanTask için bir handler'a ihtiyaç bulunuyor.
 *
 * Bu handlerlar, kullanıcı etkileşimi için gerekli UI kurallarını işletmekten
 * sorumlular.
 *
 * @author Hakan Uygun
 */
public abstract class AbstractHumanTaskHandler extends DialogBase implements Serializable {

    @Inject
    private TaskService taskService;

    @Inject
    private TaskRepository taskRepository;
    
    @Inject
    private TaskConsole taskConsole;

    @Inject
    private IdentityService identityService;
    
    @Inject
    private Identity identity;

    private TaskInfo task;
    private List<TaskResultCommand> resultCommands = new ArrayList<>();
    private List<Comment> comments;
    private String comment;

    private String delegatedUser;

    public void openDialog(TaskInfo task) {
        //FIXME: Bu method her hangi bir yerde kullanılıyor mu?
        this.task = task;
        this.comment = "";

        popVariables();

        resultCommands.clear();
        initResultButtons();

        comments = taskService.getTaskComments(task.getId());

        openDialog();
    }
    
    @Override
    protected void decorateDialog(Map<String, Object> options) {
    	options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);
    }
    
    @Override
    public void closeDialog() {
    }

    /**
     * Task'ı verilen sonucu ekleyerek kapatır.
     *
     * @param result
     */
    public void closeTask(String result) {

        onBeforeClose(result);

        saveComment();

        pushVariables();

        //task.getVariables().put("RESULT", result);
        executeResultCommand(result);

        //Eğer task kimse tarafından sahiplenilmemiş ise önce claim edelim...
        if (Strings.isNullOrEmpty(task.getTask().getAssignee())) {
            Map<String,Object> localVars = new HashMap<>();
            localVars.put("NOTIFICATION", Boolean.FALSE);
            taskService.setVariablesLocal(task.getId(), localVars);
            taskService.claim(task.getId(), identity.getLoginName());
            //Claim'den sonra bir saniye bekliyelim... Bazen complete için değerler yetişemiyor.
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException ex) {
            }
        }
        
        if( task.getVariables().containsKey("ASSIGNEE")){
            task.getVariables().put("ASSIGNEE", Strings.isNullOrEmpty(task.getTask().getAssignee()) ? identity.getLoginName() : task.getTask().getAssignee());
        }
        
        taskService.complete(task.getId(), task.getVariables());
        onAfterClose(result);
        taskConsole.refresh();
        
        //RequestContext.getCurrentInstance().closeDialog(null);
    }

    /**
     * ismi verilen result için register edilmiş komutu çalıştırır.
     *
     * Farklı bir davranış için override edilebilir.
     *
     * @param result
     */
    protected void executeResultCommand(String result) {
        for (TaskResultCommand trc : resultCommands) {
            if (trc.getResult().equals(result)) {
                trc.execute(task);
            }
        }
    }

    /**
     * Sahibi olmayan taskı sahiplenelim.
     */
    public void claimTask() {
        try {
            saveComment();
            taskService.claim(task.getId(), identity.getLoginName());

            //Veri tabanından tekrar alıp setleyelim. Değişiklikler yansısın.
            setTask(taskRepository.getTaskById(task.getId()));
            
        } catch (TaskAlreadyClaimedException e) {
            FacesMessages.warn("TaskAlreadyClaimedException", "TaskAlreadyClaimedException");
        }
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
            taskService.setAssignee(task.getId(), getDelegatedUser());
            taskConsole.refresh();
        }
    }

    /**
     * Task kapatıldıktan sonra çağrılır. Task handlerlar bu noktada orjinal
     * kaydı düzenleyebilir.
     *
     * @param result kapanış değerinin ne olduğu
     */
    protected void onAfterClose(String result) {
        //Alt sınıflar override etsin diye var.
    }

    /**
     * Task kapanmadan önce çağrılır. Task handlerlar bu noktada orjinal kaydı
     * düzenleyebilir.
     *
     * @param result kapanış değerinin ne olduğu
     */
    protected void onBeforeClose(String result) {
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
    protected void onBeforeSave() {

    }

    /**
     * Task değerleri save edildikten hemen sonra çağrılır.
     */
    protected void onAfterSave() {

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
        this.task = task;

        this.comment = "";
        popVariables();
        resultCommands.clear();
        initResultButtons();

        comments = taskService.getProcessInstanceComments(task.getTask().getProcessInstanceId());
    }

    /**
     * UI'dan alınan comment'i task'a ekler.
     */
    protected void saveComment() {
        if (!Strings.isNullOrEmpty(comment)) {
            //FIXME: userID işini ne yapacağız?
            identityService.setAuthenticatedUserId(identity.getLoginName());
            Comment c = taskService.createComment(task.getId(), task.getTask().getProcessInstanceId(), comment);
        }
        comment = "";
    }

    /**
     * Task yükleme sonrası task variablelarını okumak için.
     *
     * Tüm degerler task.getVariables() ile alınabilir ama bu esnada bir değerin
     * alınıp işlenmesi için kulanılır.
     *
     */
    protected void popVariables() {

    }

    /**
     * Task bitiminde task sonuç variablelarını geri basmak için.
     *
     * save'den hemen önce process engine bir değer basmak isteniyor ise
     * kullanılır.
     */
    protected void pushVariables() {

    }

    /**
     * UI üzerinde çıkacak olan kapatma düğmelerini ayarlar.
     *
     * Farklı türden sonuçlar dönecekse bu method override edilmeli.
     */
    protected void initResultButtons() {

        String s = task.getAcceptableResults();

        //Değer yoksa varsayılan COMPLETE
        if (Strings.isNullOrEmpty(s)) {
            resultCommands.add(TaskResultCommand.COMPLETE);
            return;
        }

        List<String> ls = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(s);

        //Şimdi her komut karşılığı bulunup düğme oluşturulacak
        for (String cmd : ls) {
            TaskResultCommand c = TaskResultCommandRegistery.getCommand(cmd);
            //Eğer gelen komut tanımlı ise ekleyelim. Yoksa NPE!
            if (c != null) {
                resultCommands.add(c);
            }
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

    protected TaskService getTaskService() {
        return taskService;
    }

    /**
     * Claim Buttonunun görünüp görünmeyeceği...
     *
     * Eğer üzerinde Assignee yoksa sahiplenelim...
     *
     * @return
     */
    public Boolean showClaimButton() {
        return Strings.isNullOrEmpty(getTask().getTask().getAssignee());
    }

    /**
     * Delegate Buttonunun görünüp görünmeyeceği.
     *
     * Eğer task kendine ait değil ise başka birine delegate edemez.
     *
     * TODO: Yetki konusu burada nasıl işleyecek? TODO: BA kavramı burada nasıl
     * işleyecek?
     *
     * @return
     */
    public Boolean showDelegateButton() {
        //Eğer taskın sahibi kendisi ise
        return identity.getLoginName().equals(getTask().getTask().getAssignee());
    }

    public String getDelegatedUser() {
        return delegatedUser;
    }

    public void setDelegatedUser(String delegatedUser) {
        this.delegatedUser = delegatedUser;
    }
    
    public Boolean isCommentRequired(){
        return ((HumanTaskHandler)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(HumanTaskHandler.class))).commentRequired();
    }

}
