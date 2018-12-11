/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.FeaturePointer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * En düşük öncelikli Default AttachmentContextProvider.
 * 
 * Her türlü FeaturePointer kabul eder.
 * Eğer payload FeaturePointer ise kabul eder.
 * 
 * priority = 500
 * 
 * ContextRoot = /feature/featureBK şeklinde oluşturulur.
 * 
 * Eğer session varsa Identity.loginName UserName olarak atanır.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class FeatureAttachmentContextProvider implements AttachmentContextProvider{

    @Inject
    private Identity identity;
    
    @Override
    public boolean canHandle(Object payload){
        //Eğer payload bir featurePointer ise
        return payload instanceof FeaturePointer;
    }
    
    @Override
    public boolean canHandle(FeaturePointer featurePointer, Object payload) {
        //Payload ile ilgilenmiyoruz.
        return false;
    }

    @Override
    public boolean canHandle(FeaturePointer featurePointer) {
        //Hertürlü feature kabul
        return true;
    }
    
    @Override
    public int priority() {
        return 500;
    }

    @Override
    public AttachmentContext getContext(FeaturePointer featurePointer, Object payload) {
        AttachmentContext result = new AttachmentContext();
        
        if( featurePointer != null ){
            result.setRoot( "/" + featurePointer.getFeature() + "/" + featurePointer.getBusinessKey());
            result.setFeaturePointer(featurePointer);
        } else if( payload instanceof FeaturePointer){
            FeaturePointer fp = (FeaturePointer)payload;
            result.setRoot( "/" + fp.getFeature() + "/" + fp.getBusinessKey());
            result.setFeaturePointer(fp);
        } else {
            result.setRoot( "/attic" );
        }
        
        if( identity != null ){
            result.setUsername(identity.getLoginName());
        }
        
        return result;
    }

    
    
}
