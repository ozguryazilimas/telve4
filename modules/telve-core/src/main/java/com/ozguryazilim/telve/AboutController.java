package com.ozguryazilim.telve;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import org.primefaces.PrimeFaces;


/**
 * About Dialogu için UI controller.
 * 
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class AboutController implements Serializable{
    
    public void openDialog(){
            
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);
        
        PrimeFaces.current().dialog().openDynamic("/layout/aboutPopup", options, null);
    }
    
    public void closeDialog() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
}
