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
public class DimensionBuilder {
   
    private Dimension dimension;
    
    public static DimensionBuilder create( String dimensionName, String baseUnit ){
        DimensionBuilder builder = new DimensionBuilder();
        builder.dimension = new CustomDimension(dimensionName);
        builder.dimension.addBaseUnit(baseUnit);
        return builder;
    }
    
    public DimensionBuilder addUnit( String unitName, Quantity base ){
        dimension.addUnit(unitName, base);
        return this;
    }
    
    public DimensionBuilder addUnit( Unit unit ){
        dimension.addUnit(unit);
        return this;
    }
    
    public void register(){
        DimensionRegistery.register(dimension);
    }
    
}
