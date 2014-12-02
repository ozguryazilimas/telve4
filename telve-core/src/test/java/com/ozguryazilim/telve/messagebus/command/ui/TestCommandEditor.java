/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

/**
 *
 * @author haky
 */
public class TestCommandEditor extends CommandEditorBase<TestCommand>{

    @Override
    public TestCommand createNewCommand() {
        return new TestCommand();
    }
    
}
