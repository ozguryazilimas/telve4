/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import com.ozguryazilim.telve.api.module.TelveModule;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynaForm Module
 * @author Hakan Uygun
 */
@TelveModule
public class TelveDynaFormModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveDynaFormModule.class);
    
    @Inject
    private DynaFormManager dynaFormManager;
    
    @PostConstruct
    public void init(){
        LOG.info("Telve Dyna Form Module Loaded!");
        
        dynaFormManager.getForms("");
    }
}
