package com.ozguryazilim.telve.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author oyas
 */
@Entity
@Table(name = "TLV_ATTACHMENT")
public class Attachment implements Serializable{
    
    @Id @Column(name = "ID")
    private String id;
    
    @Column(name = "MIME_TYPE")
    private String mimeType;
    
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "PATH")
    private String path;
    
    @Column(name = "CREATED_BY")
    private String createBy;
    
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    //Meta Data Alanları
    @Column(name = "FEATURE")
    private String feature;  //Feature
    
    @Column(name = "FEATURE_BK")
    private String featureBK; //Business Key
    
    @Column(name = "FEATURE_PK")
    private Long featurePK;        //Primary Key     
    
    //Bu alanların sunucu ( CMIS/JCR tarafında ki arayüzler için önemi var.)
    @Column(name = "TITLE")
    private String title; //Caption, Topic v.b.
    @Column(name = "INFO")
    private String info;
    @Column(name = "CATEGORY")
    private String category;
    
    //virgüllerle ayrılmış string
    @Column(name = "TAGS")
    private String tags;

    //Link tipindekiler için content basit bir string olacak aslında onu alabilir miyiz?
    @Column(name="STR_CONTENT")
    private String content; //Sadece link tipindekiler için anlamlı olacaktır. JPA versionu da burayı kullanabilir mi? BLOB olarak mesela?
    
    //FIXME: liste olmasına gerek var mı? Content aslında varsa sadece 1 tane olacak. OneToOne mı yapsak?
    @OneToMany(mappedBy = "attachment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AttachmentContent> contents = new ArrayList<>();

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AttachmentContent> getContents() {
        return contents;
    }

    public void setContents(List<AttachmentContent> contents) {
        this.contents = contents;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
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
        final Attachment other = (Attachment) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
