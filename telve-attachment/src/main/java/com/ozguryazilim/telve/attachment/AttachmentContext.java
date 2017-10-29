/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment;

import com.ozguryazilim.telve.entities.FeaturePointer;
import java.io.Serializable;

/**
 * Attachment işlemleri için temel bağlam bilgileri modeli.
 * 
 * @author oyas
 */
public class AttachmentContext implements Serializable{

    /**
     * Kullanıcı ismi
     */
    private String username;
    
    /**
     * İlgili context için root path'i
     */
    private String root;
    
    private FeaturePointer featurePointer;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FeaturePointer getFeaturePointer() {
        return featurePointer;
    }

    public void setFeaturePointer(FeaturePointer featurePointer) {
        this.featurePointer = featurePointer;
    }
    
    
    
}
