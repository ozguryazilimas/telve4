package com.ozguryazilim.telve.messagebus.command;

import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.messagebus.command.ui.ScheduledCommandBrowse;
import com.ozguryazilim.telve.messagebus.command.ui.ScheduledCommandUIModel;
import com.ozguryazilim.telve.messagebus.command.ui.StoredCommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Named
@Dependent
public class ScheduledCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledCommandService.class);

    @Inject
    private Identity identity;

    @Inject
    private AuditLogger auditLogger;

    @Inject
    private CommandSender commandSender;

    @Inject
    private StoredCommandRepository storedCommandRepository;


    @Transactional
    public StoredCommand getStoredCommandById(long id) {
        return storedCommandRepository.findBy(id);
    }

    @Transactional
    public List<StoredCommand> getStoredCommands() {
        return storedCommandRepository.findAll();
    }

    /**
     * StoredCommand Id's ile ilgili command ı bulup çalıştırır.
     * @param id StoredCommand Id'si
     * @return Command başarılı şekilde çalıştırılabilirse true döner
     * @throws ClassNotFoundException
     */
    @Transactional
    public boolean runById(long id) {
        StoredCommand command = getStoredCommandById(id);

        if (command != null) {
            Command cmnd = null;
            try {
                StorableCommand storableCommand = storedCommandRepository.convert(command);
                auditLogger.actionLog("ScheduledCommand", 0l, command.getName(), "ScheduledCommand", "EXEC", identity.getLoginName(), "");
                commandSender.sendCommand(storableCommand);
            } catch (ClassNotFoundException ex) {
                LOG.error("Error while creating command", ex);
            }
            return true;
        }

        return false;
    }
}
