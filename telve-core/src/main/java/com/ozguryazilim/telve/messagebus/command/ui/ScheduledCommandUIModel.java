/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.messagebus.command.Command;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import java.util.Date;
import java.util.Objects;
import javax.ejb.ScheduleExpression;

/**
 * Zamanlanmış ( ScheduledCommand ) ve Saklanmış ( StoredCommand ) komutları bir arada aynı arayüzde sunmak için UI modeli.
 * 
 * Wrapper bir sınıftır. Kendi bilgi tutmak yerine içerdiği komut bilgilerini sunar.
 * Bir çeşit CrossJoin ile toparlanacaklar. 
 * 
 * @author Hakan Uygun
 */
public class ScheduledCommandUIModel {
    
    private String id;
    private ScheduledCommand scheduledCommand;
    private StorableCommand storableCommand;
    private StoredCommand storedCommand;
    
    private long timeRemaining;
    private String timeRemainingStr;
    private Date nextTimeout;
    private ScheduleExpression schedule;
    private String scheduleStr;
    
    public Command getCommand(){
        return storableCommand != null ? storableCommand : scheduledCommand;
    }
    
    /**
     * Geriye komut adını döndürür.
     * 
     * @return 
     */
    public String getName(){
        return getCommand().getName();
    }

    /**
     * Geriye eğer StoredCommand ise girilmiş açıklamayı döndürür.
     * @return 
     */
    public String getInfo(){
        return storedCommand != null ? storedCommand.getInfo() : "";
    }
    
    /**
     * En uygun komut bulunup değerleri döndürülür.
     * 
     * @return 
     */
    public String getParams(){
        //FIXME: komutlar için aslında sadece içerik yazan bir method icat etmeli.
        if( scheduledCommand != null ){
            return scheduledCommand.getCommand().toString();
        } else if( storableCommand != null ){
            return storableCommand.toString();
        } 
        
        return "";
    }

    /**
     * Eğer scheduledCommand tanımlıysa onun değil ise stored command'ın oluşturulma tarihini döndürür.
     * @return 
     */
    public Date getCreatedDate(){
        if( scheduledCommand != null ){
            return scheduledCommand.getCreateDate();
        } else if( storedCommand != null ){
            storedCommand.getCreateDate();
        } 
        return null;
    }
    
    /**
     * Eğer scheduledCommand tanımlıysa onun değil ise stored command'ın oluşturan bilgisini döndürür.
     * @return 
     */
    public String getCreatedBy(){
        if( scheduledCommand != null ){
            return scheduledCommand.getCreateBy();
        } else if( storedCommand != null ){
            storedCommand.getCreateBy();
        } 
        return "";
    }

    /**
     * Geriye tanımlı ScheduledCommand'ı döndürür.
     * @return 
     */
    public ScheduledCommand getScheduledCommand() {
        return scheduledCommand;
    }

    /**
     * ScheduledCommand'ı setler
     * @param scheduledCommand 
     */
    public void setScheduledCommand(ScheduledCommand scheduledCommand) {
        this.scheduledCommand = scheduledCommand;
    }

    public StorableCommand getStorableCommand() {
        return storableCommand;
    }

    public void setStorableCommand(StorableCommand storableCommand) {
        this.storableCommand = storableCommand;
    }

    public StoredCommand getStoredCommand() {
        return storedCommand;
    }

    public void setStoredCommand(StoredCommand storedCommand) {
        this.storedCommand = storedCommand;
    }

    /**
     * Geriye kalan çalışma zamanını döndürür.
     * @return 
     */
    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    /**
     * Bir sonraki çalışma tarih saatini döndürür.
     * @return 
     */
    public Date getNextTimeout() {
        return nextTimeout;
    }

    public void setNextTimeout(Date nextTimeout) {
        this.nextTimeout = nextTimeout;
    }

    /**
     * Zamanlama bilgisini döndürür.
     * @return 
     */
    public ScheduleExpression getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleExpression schedule) {
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeRemainingStr() {
        return timeRemainingStr;
    }

    public void setTimeRemainingStr(String timeRemainingStr) {
        this.timeRemainingStr = timeRemainingStr;
    }

    public String getScheduleStr() {
        return scheduleStr;
    }

    public void setScheduleStr(String scheduleStr) {
        this.scheduleStr = scheduleStr;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScheduledCommandUIModel other = (ScheduledCommandUIModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


    
    
}
