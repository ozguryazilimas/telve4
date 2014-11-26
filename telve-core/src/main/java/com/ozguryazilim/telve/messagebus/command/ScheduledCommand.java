/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import java.util.Date;

/**
 * Zamanlanmış komut komutu.
 * 
 * Her hangi bir komutu zamanlanmış bir komut haline dönüştürmek için kullanılır.
 * 
 * @author Hakan Uygun
 */
public class ScheduledCommand extends AbstractCommand{
    
    private String schedule;
    private Date createDate;
    private String createBy;
    private Command command;

    /**
     * Yeni bir zamanlanmış komut oluşturur.
     * @param schedule 
     * @param createBy
     * @param command 
     */
    public ScheduledCommand(String schedule, String createBy, Command command) {
        this.schedule = schedule;
        this.createBy = createBy;
        this.command = command;
        this.createDate = new Date();
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
    
    
}
