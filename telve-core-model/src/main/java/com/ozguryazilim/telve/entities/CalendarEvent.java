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
 * Calendar sisteminde kullnaılacak eventler için entity/model sınıfı.
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_CALENDAR")
public class CalendarEvent extends EntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;
    
    @Column(name = "INFO")
    private String info;
    
    @Column(name = "EDITABLE")
    private Boolean editable  = false;
    
    @Column(name = "ALLDAY")
    private Boolean allDay  = false;
    
    @Column(name = "DONE")
    private Boolean done = false;
    
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    /**
     * Bu event'in hangi kaynaktan geldiği.
     * 
     * Bir CDI bileşen ismi'dir. Reqistery'den bulunur.
     */
    @Column(name = "SOURCE_NAME")
    private String sourceName;
    
    /**
     * Kaynak tarafının bulması için anahtar alan
     */
    @Column(name = "SOURCE_KEY")
    private String sourceKey;
    
    /**
     * event kaynağının anlayacağı veri. 
     * 
     * Event kaynağı tarafından verilen model json olarak saklanır.
     * 
     */
    @Column(name = "SOURCE_DATA")
    private String sourceData;

    /**
     * Bu event'in hangi kullanıcı ya da rolü ilgilendirdiği.
     */
    @Column(name = "ACTOR")
    private String actor;
    
    /**
     * Hatırlatma kuralı. 
     * 
     * Format ScheduleModel içindir. @see ScheduleModel
     * 
     * Null veya Empty ise reminder yoktur.
     */
    @Column(name = "REMINDER")
    private String reminderSchedule;
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getReminderSchedule() {
        return reminderSchedule;
    }

    public void setReminderSchedule(String reminderSchedule) {
        this.reminderSchedule = reminderSchedule;
    }
    
    
    
}
