/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import com.ozguryazilim.telve.messagebus.command.AbstractCommand;
import java.util.Date;

/**
 * Cmmand tabanlı log sistemi.
 * 
 * Özellikle zamanlanmış görevlerin sonuçlarını son kullanıcıya göstermek için kullanılır.
 * 
 * @author Hakan Uygun
 */
public class ExecutionLogCommand extends AbstractCommand{
    
    private String logger;
    private Date createDate;
    private String severity;
    private String message;

    public ExecutionLogCommand() {
    }

    public ExecutionLogCommand(String logger, String severity, String message) {
        this.logger = logger;
        this.severity = severity;
        this.message = message;
        this.createDate = new Date();
    }

    
    
    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
