/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import com.ozguryazilim.telve.entities.ExecutionLog;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutionLogCommand komut çalıştırıcısı.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = ExecutionLogCommand.class)
public class ExecutionLogCommandExecutor extends AbstractCommandExecuter<ExecutionLogCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionLogCommandExecutor.class);
    
    @Inject
    private ExecutionLogRepository repository;
    
    @Override @Transactional
    public void execute(ExecutionLogCommand command) {
        LOG.info("{} {}", command.getLogger(), command.getMessage());
        ExecutionLog elog = new ExecutionLog(command.getCreateDate(), command.getLogger(), command.getSeverity(), command.getMessage());
        repository.save(elog);
    }
    
}
