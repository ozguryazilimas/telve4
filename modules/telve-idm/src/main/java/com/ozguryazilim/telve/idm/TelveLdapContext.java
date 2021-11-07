package com.ozguryazilim.telve.idm;

import java.io.Serializable;
import java.util.Objects;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;

/**
 * Birden fazla LDAP bağlantısı desteklemek için Shiro.ini'den alınacak property'leri tutan model sınıfı.
 * @author hakan
 */
public class TelveLdapContext implements Serializable{
    
     private String name;
    private String userDnTemplate;
    private LdapContextFactory contextFactory;

    public TelveLdapContext() {
        this.contextFactory = new JndiLdapContextFactory();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getUserDnTemplate() {
        return userDnTemplate;
    }

    public void setUserDnTemplate(String userDnTemplate) {
        this.userDnTemplate = userDnTemplate;
    }

    public LdapContextFactory getContextFactory() {
        return contextFactory;
    }

    public void setContextFactory(LdapContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TelveLdapContext other = (TelveLdapContext) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
