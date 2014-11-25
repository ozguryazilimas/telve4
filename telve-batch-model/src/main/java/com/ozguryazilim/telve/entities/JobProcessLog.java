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
 * Job işlemleri sırasında detay bilgiler için log satırı.
 * 
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_JOB_PROCESS_LOG")
public class JobProcessLog extends EntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "JOB_NAME")
    private String jobName;
    
    @Column(name = "INSTANCE_ID")
    private Long instanceId;
    
    @Column(name = "EXECUTION_ID")
    private Long executionId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOG_DATE")
    private Date logDate;
    
    @Column(name = "SEVERITY")
    private String severity;
    
    @Column(name = "MESSAGE")
    private String message;

    public JobProcessLog() {
    }

    public JobProcessLog(String jobName, Long instanceId, Long executionId, Date logDate, String severity, String message) {
        this.jobName = jobName;
        this.instanceId = instanceId;
        this.executionId = executionId;
        this.logDate = logDate;
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
