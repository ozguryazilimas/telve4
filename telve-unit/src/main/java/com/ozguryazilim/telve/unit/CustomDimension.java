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
public class CustomDimension extends AbstractDimension{

    private String name;

    public CustomDimension(String name) {
        this.name = name;
    }
    
    
    @Override
    public String getDimensionName() {
        return name;
    }
    
}
