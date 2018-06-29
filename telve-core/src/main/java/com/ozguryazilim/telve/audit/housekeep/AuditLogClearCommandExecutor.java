package com.ozguryazilim.telve.audit.housekeep;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozguryazilim.telve.audit.AuditLogRepository;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.utils.DateUtils;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 *
 * @author oyas
 */
@CommandExecutor(command = AuditLogClearCommand.class)
public class AuditLogClearCommandExecutor extends AbstractCommandExecuter<AuditLogClearCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogClearCommandExecutor.class);

    @Inject
    private AuditLogRepository auditLogRepository;

    @Transactional
    @Override
    public void execute(AuditLogClearCommand command) {

        Date dt = DateUtils.getDateBeforePeriod(command.getInterval(), new Date());
        String domain = command.getDomain();
        String category = command.getCategory();
        String action = command.getAction();

        LOG.info("Audit logs with following properties wil be deleted: {}",
                dt, domain, category, action);

        System.out.println(dt + domain + category + action);

        auditLogRepository.deleteByScheculedCommandParameters(dt, domain, category, action);
    }
}
