package com.ozguryazilim.telve.permisson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Permission Tanımlarını tutar
 * @author Hakan Uygun
 */
public class PermissionDefinition implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private final String name;
    private final Integer order;
    private final List<PermissionAction> actions = new ArrayList<>();

    public PermissionDefinition(String name, Integer order) {
        this.name = name;
        this.order = order;
    }

    public void addAction( PermissionAction action ){
        actions.add(action);
    }

    public String getName() {
        return name;
    }

    public Integer getOrder() {
        return order;
    }

    public List<PermissionAction> getActions() {
        return actions;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PermissionDefinition other = (PermissionDefinition) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

}
