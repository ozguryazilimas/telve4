package com.ozguryazilim.telve.attachment.modeshape.config;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.attachment.modeshape.ModeShapeRepositoryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

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
