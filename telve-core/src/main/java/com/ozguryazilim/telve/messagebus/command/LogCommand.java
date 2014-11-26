/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

/**
 * Telve MessageBus Komut Sistemini deneme komutu.
 * 
 * Verilen logu MessageBus üzerinden yazar.
 * 
 * @author Hakan Uygun
 */
public class LogCommand extends AbstractCommand{
    
    private String log;

    public LogCommand(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "LogCommand{" + "log=" + log + '}';
    }

    
}
