/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import com.ozguryazilim.telve.messagebus.command.ui.CommandEditor;
import com.ozguryazilim.telve.messagebus.command.ui.CommandEditorBase;
import com.ozguryazilim.telve.view.Pages;

/**
 * Execution Log Clear Command için editor kontrol sınıfı.
 * 
 * @author Hakan Uygun
 */
@CommandEditor(command = ExecutionLogClearCommand.class, page = Pages.Admin.ExecutionLogClearCommand.class)
public class ExecutionLogClearCommandEditor extends CommandEditorBase<ExecutionLogClearCommand>{

    @Override
    public ExecutionLogClearCommand createNewCommand() {
        ExecutionLogClearCommand cmd = new ExecutionLogClearCommand();
        //FIXME: Comfigürasyondan alalım.
        //ConfigResolver.getPropertyValue("Command.ExecutionLogClearCommand.Interval", "1000");
        cmd.setInterval(1000l);
        return cmd;
    }
    
}
