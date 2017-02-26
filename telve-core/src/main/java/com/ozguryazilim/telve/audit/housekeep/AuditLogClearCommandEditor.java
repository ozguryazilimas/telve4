/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit.housekeep;

import com.ozguryazilim.telve.messagebus.command.ui.CommandEditor;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditorBase;
import com.ozguryazilim.telve.view.Pages;

/**
 *
 * @author oyas
 */
@CommandEditor(command = AuditLogClearCommand.class, page = Pages.Admin.AuditLogClearCommand.class)
public class AuditLogClearCommandEditor extends CommandEditorBase<AuditLogClearCommand>{

    @Override
    public AuditLogClearCommand createNewCommand() {
        AuditLogClearCommand cm = new AuditLogClearCommand();
        
        cm.setInterval("30d");
        
        return cm;
    }
    
}
