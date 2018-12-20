package com.ozguryazilim.telve.permisson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bir permission için Action kısmını tanımlar.
 * 
 * domain:action:scope ayrımında action ve Scope bloğunu barındırır.
 * 
 * Scope değeri içerisinde NONE ve ALL kafadan eklenir. Ek scope'lar bu ikisinin arasına eklenir.
 * 
 * @author HakanUygun
 */
public class PermissionAction implements Serializable{

    public static final String SCOPE_NONE = "NONE";
    public static final String SCOPE_ALL = "ALL";
    
    private final String name;
    private final Integer order;
    private final List<String> scopes = new ArrayList<>();

    public PermissionAction(String name, Integer order) {
        this.name = name;
        this.order = order;
        scopes.add(SCOPE_NONE);
        scopes.add(SCOPE_ALL);
    }

    
    public PermissionAction(String name, Integer order, List<String> scopes) {
        this.name = name;
        this.order = order;
        this.scopes.add(SCOPE_NONE);
        
        scopes.forEach((s) -> {
            this.scopes.add(s);
        });
        
        this.scopes.add(SCOPE_ALL);
    }
    
    /**
     * Verilen yeni scope'u sondan bir önceki posizyona ekler.
     * @param scope 
     */
    public void addScope( String scope ){
        //Son item olarak ALL kalacak.
        scopes.add(scopes.size() - 2, scope);
    }

    public String getName() {
        return name;
    }

    public Integer getOrder() {
        return order;
    }

    public List<String> getScopes() {
        return scopes;
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.name);
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
        final PermissionAction other = (PermissionAction) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
    
}
