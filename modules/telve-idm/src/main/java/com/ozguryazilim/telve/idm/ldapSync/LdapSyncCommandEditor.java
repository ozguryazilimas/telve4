package com.ozguryazilim.telve.idm.ldapSync;

import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditor;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditorBase;

@CommandEditor(command = LdapSyncCommand.class, page = IdmPages.LdapSyncCommand.class)
public class LdapSyncCommandEditor extends CommandEditorBase<LdapSyncCommand> {

    @Override
    public LdapSyncCommand createNewCommand() {
        return new LdapSyncCommand(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    }

}
