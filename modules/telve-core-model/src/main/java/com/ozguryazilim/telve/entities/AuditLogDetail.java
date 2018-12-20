package com.ozguryazilim.telve.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * AuditLog detail model class.
 * 
 * Değişim bilgileri tutulur.
 * 
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_AUDIT_LOG_DET")
public class AuditLogDetail extends EntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "LID", foreignKey = @ForeignKey(name = "FK_AULOG_LID"))
    private AuditLog auditLog;
    
    @Column( name = "ATTR")
    private String attribute;
    
    @Column( name = "OVAL")
    private String oldValue;
    
    @Column( name = "NVAL")
    private String newValue;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuditLog getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(AuditLog auditLog) {
        this.auditLog = auditLog;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
}
