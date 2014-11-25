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
@Table(name = "TLV_JOB_EXEC_LOG")
public class JobExecutionLog extends EntityBase{

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
    @Column(name = "START_DATE")
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "PARAMS")
    private String params;
    
    @Column(name = "STARTED_BY")
    private String startedBy;

    public JobExecutionLog() {
    }

    public JobExecutionLog(String jobName, Long instanceId, Long executionId, Date startDate, String params, String startedBy) {
        this.jobName = jobName;
        this.instanceId = instanceId;
        this.executionId = executionId;
        this.startDate = startDate;
        this.params = params;
        this.startedBy = startedBy;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }
    
    
    
}
