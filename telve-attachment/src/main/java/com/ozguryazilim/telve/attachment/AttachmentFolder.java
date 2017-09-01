/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Bu model sınıf virtual folder'lar için de kullanılabilir mi?
 * @author oyas
 */
public class AttachmentFolder implements Serializable{
    
    private String id;
    private String name;
    private String path;
    private String createBy;
    private Date createDate;
    
    //Üst folder
    private AttachmentFolder parent;
    //Alt folder ağacı.
    private List<AttachmentFolder> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public AttachmentFolder getParent() {
        return parent;
    }

    public void setParent(AttachmentFolder parent) {
        this.parent = parent;
    }

    public List<AttachmentFolder> getChildren() {
        return children;
    }

    public void setChildren(List<AttachmentFolder> children) {
        this.children = children;
    }
    
    
}
