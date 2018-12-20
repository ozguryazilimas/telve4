package com.ozguryazilim.telve.feature.search;

import com.ozguryazilim.telve.entities.FeaturePointer;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author oyas
 */
public class FeatureSearchResult implements Serializable{
    
    private FeaturePointer featurePointer;
    
    private Integer score;
    private String subject;
    private String info;

    public FeatureSearchResult(FeaturePointer featurePointer, String subject, String info) {
        this.featurePointer = featurePointer;
        this.subject = subject;
        this.info = info;
        this.score = 0;
    }

    public FeatureSearchResult(String feature, String businessKey, Long primaryKey, String subject, String info) {
        this.featurePointer = new FeaturePointer();
        this.featurePointer.setFeature(feature);
        this.featurePointer.setPrimaryKey(primaryKey);
        this.featurePointer.setBusinessKey(businessKey);
        this.subject = subject;
        this.info = info;
        this.score = 0;
    }
    
    public FeaturePointer getFeaturePointer() {
        return featurePointer;
    }

    public void setFeaturePointer(FeaturePointer featurePointer) {
        this.featurePointer = featurePointer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.featurePointer);
        hash = 79 * hash + Objects.hashCode(this.score);
        hash = 79 * hash + Objects.hashCode(this.subject);
        hash = 79 * hash + Objects.hashCode(this.info);
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
        final FeatureSearchResult other = (FeatureSearchResult) obj;
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.info, other.info)) {
            return false;
        }
        if (!Objects.equals(this.featurePointer, other.featurePointer)) {
            return false;
        }
        if (!Objects.equals(this.score, other.score)) {
            return false;
        }
        return true;
    }
    
    
    
}
