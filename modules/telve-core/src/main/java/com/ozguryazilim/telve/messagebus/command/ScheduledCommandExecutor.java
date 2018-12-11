/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haky
 */
@CommandExecutor(command = ScheduledCommand.class)
public class ScheduledCommandExecutor extends AbstractCommandExecuter<ScheduledCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledCommandExecutor.class);

    @Inject
    private CommandScheduler scheduler;
    
    @Override
    public void execute(ScheduledCommand command) {
        LOG.info("Command {} scheduled as {} by {}", new Object[]{command.getCommand(), command.getSchedule(), command.getCreateBy()});
        scheduler.addToSceduler(command);
    }
    
}
