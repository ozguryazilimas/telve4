package com.ozguryazilim.telve;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

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
        
        RequestContext.getCurrentInstance().openDialog("/layout/aboutPopup", options, null);
    }
    
    public void closeDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
}
