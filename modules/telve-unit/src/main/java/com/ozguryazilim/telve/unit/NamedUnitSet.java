/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

/**
 * Veri tabanına saklanan, kullanıcı tarafından üretilen seriler.
 * 
 * @author Hakan Uygun
 */
public class NamedUnitSet extends AbstractUnitSet{

    private String name;

    public NamedUnitSet(String name) {
        this.name = name;
    }
    
    
    @Override
    public String getDimensionName() {
        return name;
    }
    
}
