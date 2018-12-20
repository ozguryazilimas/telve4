package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.entities.AuditLog;
import com.ozguryazilim.telve.entities.AuditLogDetail;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import java.util.Date;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuiditLog Commandlarını çalıştırır.
 * 
 * Gelen bilgileri AuditLog olarak kaydeder.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = AuditLogCommand.class)
public class AuditLogCommandExecutor extends AbstractCommandExecuter<AuditLogCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogCommandExecutor.class);
    
    @Inject
    private AuditLogRepository repository;
    
    @Override
    public void execute(AuditLogCommand command) {
        
        LOG.debug("AuitLog : {}", command);
        
        AuditLog auditLog = new AuditLog();
        
        auditLog.setAction(command.getAction());
        auditLog.setDate(new Date());
        auditLog.setDomain(command.getDomain());
        auditLog.setMessage(command.getMessage());
        auditLog.setPk(command.getPk());
        auditLog.setBizPK(command.getBizKey());
        auditLog.setCategory(command.getCategory());
        auditLog.setUser(command.getUser());
        
        //Detaylar doğrudan aynı model olduğu için bişi yapmıyoruz ama parent bilgilerini atıyoruz.
        for( AuditLogDetail aud : command.getItems()){
            aud.setAuditLog(auditLog);
            auditLog.getItems().add( aud );
        }
        
        repository.save(auditLog);
        
    }
    
}
