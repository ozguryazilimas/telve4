/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import com.ozguryazilim.telve.bpm.handlers.ProcessHandler;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camunda BPM Engine Startup
 * @author Hakan Uygun
 */
@Startup
@Singleton
@DependsOn("DefaultEjbProcessApplication")
public class CamundaStartup {
    
    private static final Logger LOG = LoggerFactory.getLogger(CamundaStartup.class);
    
    @Inject
    private RepositoryService repositoryService;
    
    @Inject
    private RuntimeService runtimeService;

    @PostConstruct
    public void init(){
        //FIXME: Burada registery'den yüklenecek olan şeyler bulunacak...
        
        for( ProcessHandler ph : ProcessHandlerRegistery.getHandlers() ){
            LOG.info("Deployed BPMN : {}", ph.bpmn());
            this.repositoryService.createDeployment().addClasspathResource(ph.bpmn()).deploy();
        }
        
        
    }
    
}
