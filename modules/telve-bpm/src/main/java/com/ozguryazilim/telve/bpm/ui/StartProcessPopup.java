/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.ui;

import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 * Process Başlatma popup kontrol sınıfı.
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class StartProcessPopup implements Serializable{
    
    //Kullanılacak dialog viewId
    private static final String dialogName = "/bpm/startProcessPopup";
    
    public void openDialog() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        RequestContext.getCurrentInstance().openDialog( dialogName, options, null);
    }
    
    public void closeDialog() {
        //RequestContext.getCurrentInstance().closeDialog(null);
        //openDialog();
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
