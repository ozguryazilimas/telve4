/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.ui;

import com.ozguryazilim.telve.bpm.TaskInfo;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task'ın ya da süreç'in diagramını gösterir.
 * 
 * Eğer task süreci gösteriliyor ise taskın bulunduğu durum işaretlenir.
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class DiagramPopup implements Serializable{

    
    private static final Logger LOG = LoggerFactory.getLogger(DiagramPopup.class);
    
    private static final String dialogName = "/bpm/diagram";
    
    @Inject
    private RepositoryService repositoryService;
    
    private TaskInfo task;
    private String diagramXML;
    
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String subject;
    
    public void openPopup( TaskInfo task ){
        openPopup(task.getTask().getProcessDefinitionId(), task.getTask().getTaskDefinitionKey(), task.getSubject());
    }
    
    public void openPopup( Task task ){
        openPopup(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), task.getName());
    }
    
    public void openPopup( Task task, String subject ){
        openPopup(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), subject);
    }
    
    public void openPopup( HistoricTaskInstance task, String subject ){
        openPopup(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), subject);
    }
    
    public void openPopup( String processDefinitionId, String taskDefinitionKey, String subject ){
        
        this.processDefinitionId = processDefinitionId;
        this.subject = subject;
        this.taskDefinitionKey = taskDefinitionKey;
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);

        //İlgili task için dagram xml'ini okuyalım.
        diagramXML = "";    
        try {
            diagramXML = IOUtils.toString(repositoryService.getProcessModel(getProcessDefinitionId()));
        } catch (IOException ex) {
            LOG.error("Process Diagram connat read", ex ); 
        }
        
        RequestContext.getCurrentInstance().openDialog( dialogName, options, null);
    }

    public String getDiagramXML() {
        return diagramXML;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    
}
