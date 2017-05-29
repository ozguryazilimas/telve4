/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.ui;

import com.ozguryazilim.telve.bpm.TaskPages;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import com.ozguryazilim.telve.view.DialogBase;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Process Başlatma popup kontrol sınıfı.
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class StartProcessPopup extends DialogBase implements Serializable{
    
    @Override
    protected void decorateDialog(Map<String, Object> options) {
    	options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 450);
    }
    
    @Override
    public void closeDialog() {

    }
    
	@Override
	public Class<? extends ViewConfig> getDialogViewConfig() {
		return TaskPages.StartProcessPopup.class;
	}

    /**
     * Sistemde tanımlı UI üzerinden başlatılabilecek process handler listesini döndürür.
     * 
     * TODO: UI ile başlatılma kontrolü yapılmalı
     * TODO: Burada hak kontrolü de yapmak lazım sanırım.
     * 
     * @return 
     */
    public List<String> getProcessLists(){
        return ProcessHandlerRegistery.getProcessHandlerNames();
    }
    
    /**
     * Geriye ismi verilen process için tanımlı ikon yolunu döndürür.
     * @param name
     * @return 
     */
    public String getIconPath( String name ){
        return ProcessHandlerRegistery.getIconPath(name);
    }
}
