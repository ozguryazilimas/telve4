package com.ozguryazilim.telve.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Veri tabanında bir feature'ı işaretlemek için saklanması gereken minimum bilgi için veri modeli.
 * 
 * @see Feature
 * @see FeatureLink
 * 
 * @author Hakan Uygun
 */
@Embeddable
public class FeaturePointer implements Serializable{
    
    @Column( name = "FEATURE")
    private String feature;
    
    @Column( name = "FEATURE_BK")
    private String businessKey;
    
    @Column( name = "FEATURE_PK")
    private Long   primaryKey;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.feature);
        hash = 53 * hash + Objects.hashCode(this.primaryKey);
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
        final FeaturePointer other = (FeaturePointer) obj;
        if (!Objects.equals(this.feature, other.feature)) {
            return false;
        }
        if (!Objects.equals(this.primaryKey, other.primaryKey)) {
            return false;
        }
        return true;
    }
    
    
}
