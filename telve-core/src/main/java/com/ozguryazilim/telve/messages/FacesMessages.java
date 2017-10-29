/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.messages;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * FacesMessages sistemi için Utility sınıf.
 * 
 * @author Hakan Uygun
 */
public class FacesMessages {

    /**
     * Global FacesMesage for Info severity with i18n.
     * @param summary bundle key
     */
    public static void info( String summary ){
        info(null, summary, null);
    }
    
    /**
     * Global FacesMesage for Info severity with i18n.
     * @param summary bundle key
     * @param detail bundle key
     */
    public static void info( String summary, String detail ){
        info(null, summary, detail);
    }
    
    /**
     * FacesMesage for Info severity with i18n.
     * @param clientId client id
     * @param summary bundle key
     * @param detail bundle key
     */
    public static void info( String clientId, String summary, String detail ){
        message( FacesMessage.SEVERITY_INFO, clientId, getMessages().get(summary), getMessages().get(detail) );
    }
    
    /**
     * Global FacesMesage for Warn severity with i18n.
     * @param summary bundle key
     */
    public static void warn( String summary ){
        warn(null, summary, null);
    }
    
    /**
     * Global FacesMesage for Warn severity with i18n.
     * @param summary bundle key 
     * @param detail bundle key
     */
    public static void warn( String summary, String detail ){
        warn(null, summary, detail);
    }
    
    /**
     * FacesMesage for Warn severity with i18n.
     * @param clientId client id
     * @param summary bundle key
     * @param detail bundle key
     */
    public static void warn( String clientId, String summary, String detail ){
        message( FacesMessage.SEVERITY_WARN, clientId, getMessages().get(summary), getMessages().get(detail) );
    }
    
    /**
     * Global FacesMesage for Error severity with i18n.
     * @param summary bundle key
     */
    public static void error( String summary ){
        error(null, summary, null);
    }
    
    /**
     * Global FacesMesage for Error severity with i18n.
     * @param summary bundle key
     * @param detail bundle key
     */
    public static void error( String summary, String detail ){
        error(null, summary, detail);
    }
    
    /**
     * FacesMesage for Error severity with i18n.
     * @param clientId client id
     * @param summary bundle key
     * @param detail bundle key
     */
    public static void error( String clientId, String summary, String detail ){
        message( FacesMessage.SEVERITY_ERROR, clientId, getMessages().get(summary), getMessages().get(detail) );
    }
    
    /**
     * FacesMesage for current context
     * @param severity severity
     * @param clientId client id
     * @param summary message
     * @param detail message
     */
    public static void message( FacesMessage.Severity severity, String clientId, String summary, String detail ){
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, summary, detail));
    }
    
    /**
     * Returns i18n message bundles
     * @return 
     */
    private static Map<String,String> getMessages(){
        //Zaten bir session olduğu için kullanıcı locale'ini kullanmakta bir sakınca yok.
        return Messages.getMessages();
    }
    
}
