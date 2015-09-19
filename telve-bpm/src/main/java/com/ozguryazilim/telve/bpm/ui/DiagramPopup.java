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
    
    public void openPopup( TaskInfo task ){
        
        this.task = task;
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);

        //İlgili task için dagram xml'ini okuyalım.
        diagramXML = "";    
        try {
            diagramXML = IOUtils.toString(repositoryService.getProcessModel(task.getTask().getProcessDefinitionId()));
        } catch (IOException ex) {
            LOG.error("Process Diaram connat read", ex ); 
        }
        
        RequestContext.getCurrentInstance().openDialog( dialogName, options, null);
    }

    public TaskInfo getTask() {
        return task;
    }

    public void setTask(TaskInfo task) {
        this.task = task;
    }

    public String getDiagramXML() {
        return diagramXML;
    }
    
}
