package com.ozguryazilim.telve.notify.cleanupexpirednotifiescommand;

import com.ozguryazilim.telve.messagebus.command.ui.CommandEditor;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditorBase;
import com.ozguryazilim.telve.view.Pages;

@CommandEditor(command = CleanupExpiredNotifiesCommand.class, page = Pages.CleanupExpiredNotifiesCommand.class)
public class CleanupExpiredNotifiesCommandEditor extends CommandEditorBase<CleanupExpiredNotifiesCommand> {

    @Override
    public CleanupExpiredNotifiesCommand createNewCommand() {
        return new CleanupExpiredNotifiesCommand();
    }

}
