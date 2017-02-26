/*
 * Copyleft 2007-2010 Uygun Teknoloji
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * www.uygunteknoloji.com.tr
 */
package com.ozguryazilim.telve.permisson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PemissionGrouplarını tutar.
 *
 * @author Hakan Uygun
 */
public class PermissionGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String name;
    private Integer order;
    
    private final List<PermissionDefinition> definitions = new ArrayList<>();
    private final List<PermissionAction> actions = new ArrayList<>();

    public PermissionGroup(String name, Integer order) {
        this.name = name;
        this.order = order;
    }

    public List<PermissionDefinition> getDefinitions() {
        return definitions;
    }

    /**
     * Bu grup içerisinde bulunan her action'ın listesi
     * @return 
     */
    public List<PermissionAction> getActions() {
        return actions;
    }

    
    public String getName() {
        return name;
    }


    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PermissionGroup other = (PermissionGroup) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

}
