package com.ozguryazilim.telve.idm.ldapSync;

import javax.naming.ldap.LdapContext;

public class LdapSyncContext {
    private final LdapContext ldapContext;
    private final int pageSize;
    private final String scope;

    public LdapSyncContext(LdapContext ldapContext, int pageSize, String scope) {
        this.ldapContext = ldapContext;
        this.pageSize = pageSize;
        this.scope = scope;
    }

    public LdapContext getLdapContext() {
        return ldapContext;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getScope() {
        return scope;
    }
}
