/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import java.util.HashMap;
import java.util.Map;
import org.primefaces.context.RequestContext;

/**
 *
 * @author haky
 */
public abstract class AbstractDialogProcessHandler extends AbstractProcessHandler{
    
    public void openDialog() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        RequestContext.getCurrentInstance().openDialog( getDialogName(), options, null);
    }
    
    public void closeDialog() {
        startProcess();
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }
        
    public abstract String getDialogName();
}
