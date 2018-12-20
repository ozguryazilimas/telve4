package com.ozguryazilim.telve.feature;

import java.io.Serializable;
import java.util.Objects;

/**
 * Bir FeatureLink verilen bilgiler ile doğru view ekranına erişimi sağlayacak.
 * 
 * Bunun için FeatureName ve Payload'nı kullanacak
 * 
 * 
 * @author Hakan Uygun
 */
public class FeatureLink implements Serializable{
    
    /**
     * Hangi feature'ı adreslediği
     */
    private String featureName;
    
    /**
     * O feature'ın instance id'si
     */
    private Serializable id;
    
    private String caption;
    private String businessKey;
    private String icon;

    public FeatureLink(String featureName, Serializable id, String caption, String businessKey, String icon) {
        this.featureName = featureName;
        this.id = id;
        this.caption = caption;
        this.businessKey = businessKey;
        this.icon = icon;
    }
    
    public String getFeatureName() {
        return featureName;
    }

    
    public Serializable getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.featureName);
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final FeatureLink other = (FeatureLink) obj;
        if (!Objects.equals(this.featureName, other.featureName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
}
