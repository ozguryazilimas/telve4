/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Telve MessageBus Komut Sistemini deneme komutu.
 * 
 * Gelen içeriği sistem loguna yazar. 
 * 
 * Map içerisinden "LOG" anahtarı bekler.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = LogCommand.class, endpoint = "bean:logCommandExecutor")
public class LogCommandExecutor extends AbstractCommandExecuter<LogCommand>{
    
    private static final Logger LOG = LoggerFactory.getLogger(LogCommandExecutor.class);
    
    @Override
    public void execute(LogCommand command) {
        LOG.info("LOGGER: {}", command.getLog());
    }
    
}
