/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.entities;

import java.util.Properties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * JBatch Job'larına gönderilecek parametre ve zamanlama tanımları
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_JOB_DEFINITION")
public class JobDefinition extends ParamEntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    @Column(name="PROPERTIES") @Lob
    private Properties properties;
    
    @Column(name= "JOB_NAME")
    private String jobName;
    
    @Column(name = "SCHEDULE")
    private String schedule;
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
}
