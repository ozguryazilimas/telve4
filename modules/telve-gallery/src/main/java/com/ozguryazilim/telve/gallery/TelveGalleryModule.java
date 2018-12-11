/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.gallery;

import com.ozguryazilim.telve.api.module.TelveModule;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Telve Gallery Module Definition.
 * 
 * This module requires Telve JCR Module for content repository
 * @author Hakan Uygun
 */
@TelveModule
public class TelveGalleryModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveGalleryModule.class);
    
    @PostConstruct
    public void init(){
        GalleryRegistery.register("Telve.Generic.Gallery");
    }
}
