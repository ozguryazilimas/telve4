package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Arka plan rapor çalıştırma.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = ReportCommand.class)
public class ReportCommandExecutor extends AbstractCommandExecuter<ReportCommand>{
    
    private static final Logger LOG = LoggerFactory.getLogger(ReportCommandExecutor.class);

    @Override
    public void execute(ReportCommand command) {
        
        LOG.info("Rapor Çalıştırılacak : {} - {}", command.getName(), command.getEngine());
        ScheduledReportHandler handler = (ScheduledReportHandler) BeanProvider.getContextualReference(command.getEngine(), true);
        
        handler.execute(command);
        
    }
    
}
