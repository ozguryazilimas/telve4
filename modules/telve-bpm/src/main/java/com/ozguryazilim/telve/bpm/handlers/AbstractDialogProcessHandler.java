package com.ozguryazilim.telve.bpm.handlers;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.PrimeFaces;


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

        PrimeFaces.current().dialog().openDynamic( getDialogName(), options, null);
    }
    
    public void closeDialog() {
        startProcess();
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void cancelDialog() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
        
    public abstract String getDialogName();

    protected abstract void startProcess();
}
