/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import java.util.ArrayList;
import org.camunda.bpm.engine.cdi.CdiJtaProcessEngineConfiguration;
import org.camunda.bpm.engine.cdi.impl.event.CdiEventSupportBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;

/**
 * Camunda için event listener tanıtmak için.
 * 
 * @author Hakan Uygun
 */
public class TelveProcessEngineConfiguration extends CdiJtaProcessEngineConfiguration{

    @Override
    protected void init() {
        if( getCustomPostBPMNParseListeners() == null ){
            setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
        }
        
        getCustomPostBPMNParseListeners().add(new CdiEventSupportBpmnParseListener());
        
        super.init(); 
    }
    
    
    
}
