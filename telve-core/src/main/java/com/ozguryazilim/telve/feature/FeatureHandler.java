/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import com.ozguryazilim.telve.entities.ViewModel;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Feature'lar için API
 * 
 * @author Hakan Uygun
 */
public interface FeatureHandler {
    
    
    Class<? extends ViewConfig> goView( Long id );

    /**
     * Verilen Model için kendini gösteren bir FeatureLink üretecek.
     * @param entity
     * @return 
     */
    FeatureLink getFeatureLink( ViewModel entity );
    
    /**
     * Bu feature istenilen yeteneğe sahipse true aksi halde fale döner.
     * 
     * @param capability
     * @return 
     */
    Boolean hasCapability( FeatureCapability capability );
    
    /**
     * Tanımlanmış olan caption Stringi döndürür
     * @return 
     */
    String getCaption();
    
    /**
     * Tanımlanmış olan ismi döndürür.
     * @return 
     */
    String getName();
}
