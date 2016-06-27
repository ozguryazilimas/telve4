/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin;

import org.picketlink.idm.model.IdentityType;

/**
 * Identity olayları ile ilgili event.
 * 
 * Genelde Role / User silme için kullanılır.
 * 
 * @author Hakan Uygun
 */
public class IdentityEvent {
    
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    public static final String FROM_ROLE = "Role";
    public static final String FROM_USER = "User";
    
    private String from = FROM_ROLE;
    private IdentityType identity;
    private String action = CREATE;

    public IdentityEvent(IdentityType identity) {
        this.identity = identity;
    }
    
    public IdentityEvent(IdentityType identity, String action) {
        this.identity = identity;
        this.action = action;
    }
    
    public IdentityEvent(IdentityType identity, String from, String action) {
        this.identity = identity;
        this.action = action;
        this.from = from;
    }

    public IdentityType getIdentity() {
        return identity;
    }

    public String getAction() {
        return action;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    
}
