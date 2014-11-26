/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus;

import com.ozguryazilim.telve.messagebus.command.Command;

/**
 *
 * @author haky
 */
public class TestCommand implements Command{

    @Override
    public String getName() {
        return "TestCommand";
    }
    
}
