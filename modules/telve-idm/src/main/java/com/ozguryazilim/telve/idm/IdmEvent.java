package com.ozguryazilim.telve.idm;

/**
 * Identity olayları ile ilgili event.
 * 
 * Genelde Role / User silme için kullanılır.
 * 
 * @author Hakan Uygun
 */
public class IdmEvent {
    
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    public static final String FROM_ROLE = "Role";
    public static final String FROM_USER = "User";
    public static final String FROM_GROUP = "Group";
    
    private String from = FROM_ROLE;
    private String action = CREATE;
    private String subject;

    public IdmEvent(String from, String action, String subject) {
        this.action = action;
        this.from = from;
        this.subject = subject;
    }

    public String getAction() {
        return action;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    
}
