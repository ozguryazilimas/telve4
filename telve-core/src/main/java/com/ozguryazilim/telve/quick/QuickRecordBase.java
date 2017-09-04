/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.quick;

import java.io.Serializable;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuickRecord bileşenleri için temel API.
 * 
 * Bütün quickRecord bileşenleri bu sınıfı miras almalıdır.
 * 
 * @author Hakan Uygun
 */
public abstract class QuickRecordBase implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(QuickRecordBase.class);
    
    @Inject
    private QuickRecordController controller;
    
    @Inject
    private GroupedConversation conversation;
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    
    /**
     * Gerçek saklama işlemi.
     * 
     * Eğer başarılı ise geriye true değil ise false dönmeli.
     * 
     * @return 
     */
    protected abstract boolean doSave();
    
    public void save(){
        if( doSave() ){
            controller.setName(null);
            conversation.close();
            LOG.debug("Quick Record Saved");
        } 
    }
    
    public void cancel(){
        controller.setName(null);
        conversation.close();
        LOG.debug("Quick Record Canceled");
    }
    
    public String getPage(){
        return getPageViewId();
    }

    public String getPageViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getPageConfig()).getViewId();
    }
    
    public Class<? extends ViewConfig> getPageConfig() {
        return this.getClass().getAnnotation(QuickRecord.class).page();
    }
}
