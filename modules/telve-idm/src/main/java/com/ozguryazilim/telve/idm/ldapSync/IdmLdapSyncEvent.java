package com.ozguryazilim.telve.idm.ldapSync;

/**
 *
 * @author hakan
 */
public class IdmLdapSyncEvent {

    public static final String ROLE = "Role";
    public static final String USER = "User";
    public static final String GROUP = "Group";
    
    private String syncType;

    public IdmLdapSyncEvent( String syncType ) {
        this.syncType = syncType;
    }

    public String getSyncType() {
        return syncType;
    }
    
}
