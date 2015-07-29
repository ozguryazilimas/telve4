/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Uygulama notelarını saklamak için model/entity sınıfı.
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_NOTE")
public class Note extends EntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    
    @Column( name ="SUBJECT")
    private String subject;
    
    /**
     * Asıl mesaj
     */
    @Column( name ="BODY", length = 2000)
    private String body;
    
    /**
     * Notu kimin yazdığı
     */
    @Column( name = "OWNER")
    private String owner;
    
    /**
     * Kimler tarafından görülebileceği.
     * OWNER, ALL, görebililecek kişinin loginname',
     */
    @Column( name = "PERMISSION")
    private String permission; 
    
    /**
     * Oluşturulma tarihi.
     */
    @Column(name="CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    /**
     * Hangi view/belgeye attach edildiği.
     */
    @Column(name = "ATTACHMENT")
    private String attachtment;
    
    /**
     * info, warn, error değerleri alır.
     */
    @Column(name = "PRIORITY")
    private String priority;
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAttachtment() {
        return attachtment;
    }

    public void setAttachtment(String attachtment) {
        this.attachtment = attachtment;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    
}
