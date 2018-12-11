/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AuditLog'lar için model sınıfı.
 * 
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_AUDIT_LOG")
public class AuditLog extends EntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TS")
    private Date date;
    
    @Column(name = "DNAME")
    private String domain;
    
    @Column(name = "DPK")
    private Long pk;
    
    @Column(name = "DBK")
    private String bizPK;
    
    @Column(name = "ACT")
    private String action;
    
    @Column(name = "CAT")
    private String category;
    
    @Column(name = "UID")
    private String user;
    
    @Column(name = "MSG")
    private String message;
    
    @OneToMany(mappedBy = "auditLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AuditLogDetail> items = new ArrayList<AuditLogDetail>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBizPK() {
        return bizPK;
    }

    public void setBizPK(String bizPK) {
        this.bizPK = bizPK;
    }

    public List<AuditLogDetail> getItems() {
        return items;
    }

    public void setItems(List<AuditLogDetail> items) {
        this.items = items;
    }
    
    
}
