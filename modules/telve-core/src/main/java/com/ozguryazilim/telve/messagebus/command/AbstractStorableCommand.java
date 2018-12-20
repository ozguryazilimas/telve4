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
