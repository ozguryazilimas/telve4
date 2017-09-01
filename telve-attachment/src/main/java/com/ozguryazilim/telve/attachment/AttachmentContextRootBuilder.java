/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment;

import com.ozguryazilim.telve.entities.FeaturePointer;

/**
 *
 * @author oyas
 */
public interface AttachmentContextRootBuilder {
    
    /**
     * FeaturePointer'a göre kırılım yapar.
     * @param featurePointer
     * @return 
     */
    public String getRoot( FeaturePointer featurePointer );
    
}
