/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import java.io.Serializable;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;

/**
 * OptionPane kontrol sınıfları için tabamn sınıf.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractOptionPane implements Serializable{
    
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    

    /**
     * OptionPane için sınıf annotationı üzerinden aldığı Page ID'sini döndürür.
     *
     * @return
     */
    public String getOptiponPageViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getOptionPage()).getViewId();
    }

    /**
     * Sınıf işaretçisinden @Lookup page bilgisini alır
     *
     * @return
     */
    public Class<? extends ViewConfig> getOptionPage() {
        return this.getClass().getAnnotation(OptionPane.class).optionPage();
    }
    
    /**
     * Pane üzerindeki bilgilerin saklanması için override edilmeli.
     */
    public void save(){
        //Override edilmeli.
    }
    
    /**
     * Pane üzerindeki bilgilerin geri alınması için override edilmeli.
     */
    public void cancel(){
        //Override edilmeli.
    }
    
}
