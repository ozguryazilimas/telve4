/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import java.io.Serializable;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.util.ProxyUtils;

/**
 * Fazla veri girişi ve yetkilendirme problemleri olan kart ve benzeri veri giriş alanları için ek sayfa tanımı olanağı sunar.
 * 
 * Aslında sadece view controle ek bir sınıf ve kayıt imkanı sağlar. Gereken kontroller programcı tarafından yaplmalıdır. 
 * Çoğu zaman master home sınıfı kontrolleri kullanılır. 
 * 
 * @author Hakan Uygun
 */
public abstract class SubViewPageBase implements Serializable{

    public abstract void reload();
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    /**
     * Eğer bu subview'ın seçildiğine dair FormBase'den event geldiyse tekrar sorgu çek.
     * @param event 
     */
    public void selectionListener( @Observes SubViewSelectEvent event ){
        String s = viewConfigResolver.getViewConfigDescriptor(getViewPage()).getViewId();
        if( s.equals(event.getSubViewId())){
            reload();
        }
    }

    /**
     * Bu SubView için tanımlı viewPage'i döndürür.
     * @return 
     */
    public Class<? extends ViewConfig> getViewPage(){
        return ((SubView)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(SubView.class))).viewPage();
    }
}
