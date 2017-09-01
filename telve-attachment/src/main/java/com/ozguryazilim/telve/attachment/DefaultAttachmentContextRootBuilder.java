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
public class DefaultAttachmentContextRootBuilder implements AttachmentContextRootBuilder{

    @Override
    public String getRoot(FeaturePointer featurePointer) {
        return "/" + featurePointer.getFeature() + "/" + featurePointer.getBusinessKey();
    }
    
}
