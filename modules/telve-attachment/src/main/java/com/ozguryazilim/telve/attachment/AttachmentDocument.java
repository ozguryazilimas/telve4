package com.ozguryazilim.telve.attachment;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author oyas
 */
public class AttachmentDocument implements Serializable {

    private String id;
    private String mimeType;
    private String name;
    private String path;
    private String createBy;
    private Date createDate;
    
    //Meta Data Alanları
    private String feature;  //Feature
    private String featureBK; //Business Key
    private Long featurePK;        //Primary Key     
    
    //Bu alanların sunucu ( CMIS/JCR tarafında ki arayüzler için önemi var.)
    private String title; //Caption, Topic v.b.
    private String info;
    private String category;
    private String tags;

    //Kaynak Provider kim? Capability için
    private String provider;
    private Set<String> capabilities;

    //Link tipindekiler için content basit bir string olacak aslında onu alabilir miyiz?
    private String content; //Sadece link tipindekiler için anlamlı olacaktır. JPA versionu da burayı kullanabilir mi? BLOB olarak mesela?

    public AttachmentDocument() {
    }

    public AttachmentDocument(String name) {
        this.name = name;
    }

    public AttachmentDocument(String name, String content) {
        this.name = name;
        this.content = content;
    }

    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getFeatureBK() {
        return featureBK;
    }

    public void setFeatureBK(String featureBK) {
        this.featureBK = featureBK;
    }

    public Long getFeaturePK() {
        return featurePK;
    }

    public void setFeaturePK(Long featurePK) {
        this.featurePK = featurePK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Set<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Set<String> capabilities) {
        this.capabilities = capabilities;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    

}
