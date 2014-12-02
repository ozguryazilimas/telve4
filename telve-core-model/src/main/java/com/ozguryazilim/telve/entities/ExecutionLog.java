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
 *
 * @author haky
 */
@Entity
@Table(name = "TLV_EXEC_LOG")
public class ExecutionLog extends EntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOG_DATE")
    private Date date;
    
    @Column(name = "LOGGER")
    private String logger;
    
    @Column(name = "SEVERITY")
    private String severity;
    
    @Column(name = "EXEC_BY")
    private String excecutionBy;
    
    @Column(name = "MSG")
    private String message;

    public ExecutionLog() {
    }

    public ExecutionLog(Date date, String logger, String severity, String message) {
        this.date = date;
        this.logger = logger;
        this.severity = severity;
        this.message = message;
    }

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

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getExcecutionBy() {
        return excecutionBy;
    }

    public void setExcecutionBy(String excecutionBy) {
        this.excecutionBy = excecutionBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
