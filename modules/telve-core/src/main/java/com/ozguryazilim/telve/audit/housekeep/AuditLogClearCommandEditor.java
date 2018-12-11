package com.ozguryazilim.telve.audit.housekeep;

import com.ozguryazilim.telve.audit.AuditLogRepository;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditor;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditorBase;
import com.ozguryazilim.telve.view.Pages;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author oyas
 */
@CommandEditor(command = AuditLogClearCommand.class, page = Pages.Admin.AuditLogClearCommand.class)
public class AuditLogClearCommandEditor extends CommandEditorBase<AuditLogClearCommand> {

    @Inject
    AuditLogRepository repository;

    @Override
    public AuditLogClearCommand createNewCommand() {
        AuditLogClearCommand cm = new AuditLogClearCommand();

        cm.setInterval("30d");

        return cm;
    }

    public List<String> getCategories() {
        return repository.findDistinctCategories();
    }

    public List<String> getActions() {
        return repository.findDistinctActions();
    }
}
