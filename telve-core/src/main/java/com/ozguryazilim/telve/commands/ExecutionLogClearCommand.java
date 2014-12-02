/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import com.ozguryazilim.telve.messagebus.command.AbstractJBatchCommand;
import java.util.Properties;

/**
 * Zamanlanmış görevler ve benzeri işlemlerden oluşan execution Logları temizler.
 * 
 * JBatch komutudur.
 * 
 * @author Hakan Uygun
 */
public class ExecutionLogClearCommand extends AbstractJBatchCommand{

    /**
     * Ne kadar önceki verileri silecek?
     */
    private Long interval;
    
    @Override
    public String getJobName() {
        return "ExecutionLogClearJob";
    }

    @Override
    protected void buildProperties(Properties props) {
        props.setProperty("INTERVAL", interval.toString());
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }
    
    
}
