/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;

/**
 * UI tarafında ShecduleEvent'e ek lazım olan bilgileri tutmak için model sınıf.
 * 
 * Kaynak adı ve yönlendirme için gerekli bilgileri tutar.
 * 
 * @author Hakan Uygun
 */
public class CalendarEventMetadata implements Serializable{
    
    /**
     * Bu event'in hangi kaynaktan geldiği.
     * 
     * Bir CDI bileşen ismi'dir. Reqistery'den bulunur.
     */
    @Column(name = "SOURCE_NAME")
    private String sourceName;
    
    /**
     * Kaynak tarafının bulması için anahtar alan
     */
    @Column(name = "SOURCE_KEY")
    private String sourceKey;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.sourceName);
        hash = 31 * hash + Objects.hashCode(this.sourceKey);
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
        final CalendarEventMetadata other = (CalendarEventMetadata) obj;
        if (!Objects.equals(this.sourceName, other.sourceName)) {
            return false;
        }
        if (!Objects.equals(this.sourceKey, other.sourceKey)) {
            return false;
        }
        return true;
    }
    
    
}
