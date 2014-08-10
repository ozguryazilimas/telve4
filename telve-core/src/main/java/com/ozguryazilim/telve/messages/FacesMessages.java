/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * FacesMessages sistemi için Utility sınıf.
 * 
 * @author Hakan Uygun
 */
public class FacesMessages {
    
    
    
    public static void info( String summary ){
        info(null, summary, null);
    }
    
    public static void info( String summary, String detail ){
        info(null, summary, detail);
    }
    
    public static void info( String clientId, String summary, String detail ){
        message( FacesMessage.SEVERITY_INFO, clientId, summary, detail );
    }
    
    public static void warn( String summary ){
        warn(null, summary, null);
    }
    
    public static void warn( String summary, String detail ){
        warn(null, summary, detail);
    }
    
    public static void warn( String clientId, String summary, String detail ){
        message( FacesMessage.SEVERITY_WARN, clientId, summary, detail );
    }
    
    public static void error( String summary ){
        error(null, summary, null);
    }
    
    public static void error( String summary, String detail ){
        error(null, summary, detail);
    }
    
    public static void error( String clientId, String summary, String detail ){
        message( FacesMessage.SEVERITY_ERROR, clientId, summary, detail );
    }
    
    public static void message( FacesMessage.Severity severity, String clientId, String summary, String detail ){
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, summary, detail));
    }
    
}
