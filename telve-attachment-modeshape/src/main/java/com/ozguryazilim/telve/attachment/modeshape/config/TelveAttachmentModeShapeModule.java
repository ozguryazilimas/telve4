/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment.modeshape.config;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.attachment.modeshape.ModeShapeRepositoryFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author oyas
 */
@TelveModule
public class TelveAttachmentModeShapeModule {
    
    @PostConstruct
    public void init(){
        ModeShapeRepositoryFactory.getRepository();
    }
    
    @PreDestroy
    public void done(){
        ModeShapeRepositoryFactory.shutdown();
    }
}
