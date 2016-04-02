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
        commandSender.sendCommand(new AuditLogCommand(AuditLogCommand.ACT_AUTH, user, message));
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
        AuditLogCommand c = new AuditLogCommand(AuditLogCommand.ACT_AUTH, user, message);
        c.setDomain(type);
        c.setBizKey(targetUser);
        c.getItems().addAll(items);
        commandSender.sendCommand(c);
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
        commandSender.sendCommand(new AuditLogCommand( domain, pk, action, user, message));
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
        commandSender.sendCommand(new AuditLogCommand( domain, pk, bizKey, action, category,  user, message));
    }
    
}
