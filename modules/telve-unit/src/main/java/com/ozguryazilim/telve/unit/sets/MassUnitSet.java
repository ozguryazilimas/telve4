/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit.sets;

import com.ozguryazilim.telve.unit.AbstractUnitSet;
import com.ozguryazilim.telve.unit.UnitSetRegistery;
import com.ozguryazilim.telve.unit.QuantitativeAmount;
import com.ozguryazilim.telve.unit.Unit;
import com.ozguryazilim.telve.unit.UnitName;
import java.math.BigDecimal;

/**
 * Kütle ile ilgili birimler ( Kütle Boyutu )
 * 
 * @author Hakan Uygun
 */
public class MassUnitSet extends AbstractUnitSet{

    private static final String MASS_UNIT_SET = "MASS";
    
    //TODO: Ağırlık ve Kütle için birimlerin tamamı tanımlanacak
    public static final UnitName MILLIGRAM = new UnitName(MASS_UNIT_SET, "MILLIGRAM");
    public static final UnitName CENTIGRAM = new UnitName(MASS_UNIT_SET, "CENTIGRAM");
    public static final UnitName DECIGRAM = new UnitName(MASS_UNIT_SET, "DECIGRAM");
    public static final UnitName GRAM = new UnitName(MASS_UNIT_SET, "GRAM");
    public static final UnitName DECAGRAM = new UnitName(MASS_UNIT_SET, "DECAGRAM");
    public static final UnitName HECTOGRAM = new UnitName(MASS_UNIT_SET, "HECTOGRAM");
    public static final UnitName KILOGRAM = new UnitName(MASS_UNIT_SET, "KILOGRAM");
    
    public final static Unit MILLIGRAM_UNIT = new Unit(MILLIGRAM, new QuantitativeAmount(BigDecimal.ONE, MILLIGRAM));
    public final static Unit CENTIGRAM_UNIT = new Unit(CENTIGRAM, new QuantitativeAmount(BigDecimal.TEN, MILLIGRAM));
    public final static Unit DECIGRAM_UNIT = new Unit(DECIGRAM, new QuantitativeAmount(BigDecimal.TEN, CENTIGRAM));
    public final static Unit GRAM_UNIT = new Unit(GRAM, new QuantitativeAmount(BigDecimal.TEN, DECIGRAM));
    public final static Unit DECAGRAM_UNIT = new Unit(DECAGRAM, new QuantitativeAmount(BigDecimal.TEN, GRAM));
    public final static Unit HECTOGRAM_UNIT = new Unit(HECTOGRAM, new QuantitativeAmount(BigDecimal.TEN, DECAGRAM));
    public final static Unit KILOGRAM_UNIT = new Unit(KILOGRAM, new QuantitativeAmount(BigDecimal.TEN, HECTOGRAM));
    
    
    @Override
    public String getDimensionName() {
        return MASS_UNIT_SET;
    }

    public MassUnitSet() {
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
        UnitSetRegistery.register(new MassUnitSet());
    }
    
}
