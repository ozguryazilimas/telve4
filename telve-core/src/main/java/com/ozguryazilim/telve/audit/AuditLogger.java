/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.entities.AuditLogDetail;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * AuditLog yazımı için API
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class AuditLogger implements Serializable{

    @Inject
    private CommandSender commandSender;
    
    /**
     * AUTH logu gönderir.
     * 
     * @param user
     * @param message 
     */
    public void authLog( String user, String message ){
        if( logEnabled( AuditLogCommand.ACT_AUTH, AuditLogCommand.CAT_AUTH, "Login" )){
            commandSender.sendCommand(new AuditLogCommand(AuditLogCommand.ACT_AUTH, user, message));
        }
    }
    
    /**
     * AUTH logu gönderir.
     * 
     * @param targetUser
     * @param user
     * @param message 
     * @param items 
     */
    public void authLog( String type, String targetUser, String user, String message, List<AuditLogDetail> items ){
        if( logEnabled( AuditLogCommand.ACT_AUTH, AuditLogCommand.CAT_AUTH, type )){
            AuditLogCommand c = new AuditLogCommand(AuditLogCommand.ACT_AUTH, user, message);
            c.setDomain(type);
            c.setBizKey(targetUser);
            c.getItems().addAll(items);
            commandSender.sendCommand(c);
        }
    }
    
    /**
     * Entity action logu gönderir.
     * 
     * @param domain
     * @param pk
     * @param action
     * @param user
     * @param message 
     */
    public void actionLog( String domain, Long pk, String action, String user, String message ){
        if( logEnabled( action, AuditLogCommand.CAT_ENTITY, domain )){
            commandSender.sendCommand(new AuditLogCommand( domain, pk, action, user, message));
        }
    }
    
    /**
     * Entity action logu gönderir.
     * 
     * @param domain
     * @param pk
     * @param bizKey
     * @param category
     * @param action
     * @param user
     * @param message 
     */
    public void actionLog( String domain, Long pk, String bizKey, String category, String action, String user, String message  ){
        if( logEnabled( action, category, domain )){
            commandSender.sendCommand(new AuditLogCommand( domain, pk, bizKey, action, category,  user, message));
        }
    }
    
    /**
     * Entity detail action logu gönderir.
     * 
     * @param domain
     * @param pk
     * @param bizKey
     * @param category
     * @param action
     * @param user
     * @param message 
     */
    public void actionLog( String domain, Long pk, String bizKey, String category, String action, String user, String message, List<AuditLogDetail> items  ){
        if( logEnabled( action, category, domain )){
            AuditLogCommand cm = new AuditLogCommand( domain, pk, bizKey, action, category,  user, message);
            cm.setItems(items);
            commandSender.sendCommand(cm);
        }
    }
 
    
    protected boolean logEnabled( String action, String category, String domain ){
        //Eğer aditLog kapalı ise zaten bişi yazmayacak!
        if( "false".equals( ConfigResolver.getPropertyValue("auditLog", "true"))){
            return false;
        }
        
        // En datylı halinden yukarıya doğru test ediyoruz!
        if( "false".equals( ConfigResolver.getPropertyValue("auditLog." + action + "." + category + "." + domain, "true"))){
            return false;
        }
        
        if( "false".equals( ConfigResolver.getPropertyValue("auditLog." + action + "." + category, "true"))){
            return false;
        }
        
        if( "false".equals( ConfigResolver.getPropertyValue("auditLog." + action, "true"))){
            return false;
        }
        
        return true;
    }
}
