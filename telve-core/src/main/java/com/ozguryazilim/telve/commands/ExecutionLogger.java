/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import com.ozguryazilim.telve.messagebus.command.Command;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Execution Log gönderimi için API.
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named
public class ExecutionLogger implements Serializable{
    
    @Inject
    private CommandSender commandSender;
    
    
    public void info( Command command, String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( command.getName(), "INFO", message );
        sendLog(c);
    }
    
    public void error( Command command, String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( command.getName(), "ERROR", message );
        sendLog(c);
    }
    
    public void warn( Command command, String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( command.getName(), "WARN", message );
        sendLog(c);
    }
    
    public void debug( Command command, String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( command.getName(), "DEBUG", message );
        sendLog(c);
    }
    
    public void jobInfo( String jobName, long instanceId, long executionId,  String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( jobName + "(" + instanceId + ":" + executionId + ")", "INFO", message );
        sendLog(c);
    }
    
    public void jobError( String jobName, long instanceId, long executionId,  String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( jobName + "(" + instanceId + ":" + executionId + ")", "INFO", message );
        sendLog(c);
    }
    
    public void jobWarn( String jobName, long instanceId, long executionId,  String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( jobName + "(" + instanceId + ":" + executionId + ")", "INFO", message );
        sendLog(c);
    }
    
    public void jobDebug( String jobName, long instanceId, long executionId,  String message ){
        ExecutionLogCommand c = new ExecutionLogCommand( jobName + "(" + instanceId + ":" + executionId + ")", "INFO", message );
        sendLog(c);
    }
    
    private void sendLog( ExecutionLogCommand command ){
        commandSender.sendCommand(command);
    }
}
