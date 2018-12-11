/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

/**
 * 
 * @author Hakan Uygun
 */
public class UnitSetBuilder {
   
    private UnitSet unitSet;
    
    public static UnitSetBuilder create( String unitSetName, String baseUnit ){
        UnitSetBuilder builder = new UnitSetBuilder();
        builder.unitSet = new NamedUnitSet(unitSetName);
        builder.unitSet.addBaseUnit(baseUnit);
        return builder;
    }
    
    public UnitSetBuilder addUnit( String unitName, QuantitativeAmount base ){
        unitSet.addUnit(unitName, base);
        return this;
    }
    
    public UnitSetBuilder addUnit( Unit unit ){
        unitSet.addUnit(unit);
        return this;
    }
    
    public void register(){
        UnitSetRegistery.register(unitSet);
    }
    
}
