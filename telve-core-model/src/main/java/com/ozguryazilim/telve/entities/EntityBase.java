/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.MappedSuperclass;

/**
 * Telve içerisinde kullanılacakolan Entity'ler bu sınıfı miras alırlar.
 *
 * ID, equals, hash v.b. sağlar.
 *
 * @author Hakan Uygun
 */
@MappedSuperclass
public abstract class EntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Geriye entity id kolonunu döndürür.
     *
     * @return
     */
    public abstract Long getId();

    /**
     * id'nin null olup olmadığına göre daha önce persist edilip edilmediğini
     * döndürür.
     *
     * @return
     */
    public boolean isPersisted() {
        return getId() != null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getCanonicalName())
                .append("[id=")
                .append(getId())
                .append("]");
        return result.toString();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!getClass().isInstance(object)) {
            return false;
        }

        EntityBase other = (EntityBase) object;
        return Objects.equals(this.getId(), other.getId()) || (this.getId() != null && this.getId().equals(other.getId()));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

}
