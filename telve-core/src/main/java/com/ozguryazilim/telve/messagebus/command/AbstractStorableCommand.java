/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

/**
 * StorableCommand türü için taban sınıf.
 * @author Hakan Uygun
 */
public abstract class AbstractStorableCommand implements StorableCommand{

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    
}
