/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit.dimensions;

import com.ozguryazilim.telve.unit.AbstractDimension;
import com.ozguryazilim.telve.unit.DimensionRegistery;
import com.ozguryazilim.telve.unit.Quantity;
import com.ozguryazilim.telve.unit.Unit;
import com.ozguryazilim.telve.unit.UnitName;
import java.math.BigDecimal;

/**
 * Kütle ile ilgili birimler ( Kütle Boyutu )
 * 
 * @author Hakan Uygun
 */
public class MassDimension extends AbstractDimension{

    private static final String DIMENSION_NAME = "MASS";
    
    //TODO: Ağırlık ve Kütle için birimlerin tamamı tanımlanacak
    public static final UnitName MILLIGRAM = new UnitName(DIMENSION_NAME, "MILLIGRAM");
    public static final UnitName CENTIGRAM = new UnitName(DIMENSION_NAME, "CENTIGRAM");
    public static final UnitName DECIGRAM = new UnitName(DIMENSION_NAME, "DECIGRAM");
    public static final UnitName GRAM = new UnitName(DIMENSION_NAME, "GRAM");
    public static final UnitName DECAGRAM = new UnitName(DIMENSION_NAME, "DECAGRAM");
    public static final UnitName HECTOGRAM = new UnitName(DIMENSION_NAME, "HECTOGRAM");
    public static final UnitName KILOGRAM = new UnitName(DIMENSION_NAME, "KILOGRAM");
    
    public final static Unit MILLIGRAM_UNIT = new Unit(MILLIGRAM, new Quantity(BigDecimal.ONE, MILLIGRAM));
    public final static Unit CENTIGRAM_UNIT = new Unit(CENTIGRAM, new Quantity(BigDecimal.TEN, MILLIGRAM));
    public final static Unit DECIGRAM_UNIT = new Unit(DECIGRAM, new Quantity(BigDecimal.TEN, CENTIGRAM));
    public final static Unit GRAM_UNIT = new Unit(GRAM, new Quantity(BigDecimal.TEN, DECIGRAM));
    public final static Unit DECAGRAM_UNIT = new Unit(DECAGRAM, new Quantity(BigDecimal.TEN, GRAM));
    public final static Unit HECTOGRAM_UNIT = new Unit(HECTOGRAM, new Quantity(BigDecimal.TEN, DECAGRAM));
    public final static Unit KILOGRAM_UNIT = new Unit(KILOGRAM, new Quantity(BigDecimal.TEN, HECTOGRAM));
    
    
    @Override
    public String getDimensionName() {
        return DIMENSION_NAME;
    }

    public MassDimension() {
        super();
        addUnit(MILLIGRAM_UNIT);
        addUnit(CENTIGRAM_UNIT);
        addUnit(DECIGRAM_UNIT);
        addUnit(GRAM_UNIT);
        addUnit(DECAGRAM_UNIT);
        addUnit(HECTOGRAM_UNIT);
        addUnit(KILOGRAM_UNIT);
    }
    
    static{
        DimensionRegistery.register(new MassDimension());
    }
    
}
