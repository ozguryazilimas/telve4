/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import java.io.Serializable;
import java.util.Map;
import javax.inject.Inject;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * BPM süreç tanımları için taban sınıf. 
 * 
 * Bu sınıfı miras alarak süreç başlatma için gereken ui, kural seti, süreç ismi  v.b. tanımlar yapılır.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractProcessHandler implements Serializable{
 
    @Inject
    private RuntimeService runtimeService;
    
    
    public abstract void startProcess();
    
    /**
     * Verilen ID'li process'i verilen değerlerle başlatır.
     * @param id
     * @param values 
     */
    public void startProcess( String id, String bizKey, Map<String, Object> values ){
        ProcessInstance startProcessInstanceById = runtimeService.startProcessInstanceByKey( id, bizKey, values );
    }
}
