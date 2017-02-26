/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit.housekeep;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozguryazilim.telve.audit.AuditLogRepository;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.utils.DateUtils;

/**
 *
 * @author oyas
 */
@CommandExecutor(command = AuditLogClearCommand.class)
public class AuditLogClearCommandExecutor extends AbstractCommandExecuter<AuditLogClearCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogClearCommandExecutor.class);
    
    @Inject
    private AuditLogRepository auditLogRepository;
    
    @Override
    public void execute(AuditLogClearCommand command) {
        
        Date dt = DateUtils.getDateBeforePeriod(command.getInterval(), new Date());
        
        LOG.info("Audit Logs older than {} will be deleted.", dt );
        
        auditLogRepository.deleteBeforeDate(dt);
        
    }
    
}
