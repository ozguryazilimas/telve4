/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.entities.AuditLogDetail;
import com.ozguryazilim.telve.messagebus.command.AbstractCommand;
import java.util.ArrayList;
import java.util.List;

/**
 * AuditLog için command
 * 
 * @author Hakan Uygun
 */
public class AuditLogCommand extends AbstractCommand{
    
    public static final String CAT_ENTITY = "ENTITY"; 
    public static final String CAT_AUTH = "AUTH";
    public static final String CAT_SYSTEM = "SYSTEM";
    public static final String CAT_DEBUG = "DEBUG";
    
    public static final String ACT_AUTH = "AUTH";
    public static final String ACT_INSERT = "INSERT";
    public static final String ACT_UPDATE = "UPDATE";
    public static final String ACT_DELETE = "DELETE";
    public static final String ACT_SELECT = "SELECT";
    
    private String domain;
    private Long pk;
    private String bizKey;
    private String action;
    private String category;
    private String user;
    private String message;
    
    private List<AuditLogDetail> items = new ArrayList<>();

    /**
     * SEVERITY varsayılan olarak SEV_INFO olarak atanır.
     * 
     * @param domain
     * @param pk
     * @param action
     * @param user
     * @param message 
     */
    public AuditLogCommand(String domain, Long pk, String action, String user, String message) {
        this.domain = domain;
        this.pk = pk;
        this.action = action;
        this.user = user;
        this.message = message;
        this.category = CAT_ENTITY;
    }

    /**
     * Genelde AUTH tipi loglar için kullanılır. Domain bilgisi yoktur.
     * @param action
     * @param user
     * @param message 
     */
    public AuditLogCommand(String action, String user, String message) {
        this.action = action;
        this.user = user;
        this.message = message;
        this.category = CAT_AUTH;
    }

    
    
    public AuditLogCommand(String domain, Long pk, String bizKey,  String action, String category, String user, String message) {
        this.domain = domain;
        this.pk = pk;
        this.action = action;
        this.bizKey = bizKey;
        this.category = category;
        this.user = user;
        this.message = message;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AuditLogDetail> getItems() {
        return items;
    }

    public void setItems(List<AuditLogDetail> items) {
        this.items = items;
    }
    
}
