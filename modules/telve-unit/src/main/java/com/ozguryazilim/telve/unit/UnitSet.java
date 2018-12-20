package com.ozguryazilim.telve.unit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

/**
 * Unit tanımlarının bir araya toplanması için API
 * 
 * @author Hakan Uygun
 */
public interface UnitSet {

    void addBaseUnit(String unitName);

    void addUnit(String unitName, QuantitativeAmount base);

    void addUnit(Unit unit);

    void checkUnit(Unit unit) throws UnitException;

    void checkUnit(UnitName unitName) throws UnitException;

    String getDimensionName();

    /**
     * Bir birimden diğerine dönmek için gereken carpan ve bolen değerleri döndürür.
     *
     * ilk değer çarpan, ikinci değer ise bölen değeridir.
     *
     * @param from
     * @param to
     * @return
     */
    BigDecimal[] getFactors(UnitName from, UnitName to) throws UnitException;

    MathContext getMathContext();

    Unit getUnit(UnitName unitName);

    List<Unit> getUnitChain(UnitName from) throws UnitException;

    List<Unit> getUnitChain(Unit from) throws UnitException;

    Map<UnitName, Unit> getUnitMap();

    /**
     * Geriye ilgili UnitSet'ın sahip olduğu birim isimlerini döndürür
     * @return
     */
    List<UnitName> getUnitNames();

    void setMathContext(MathContext mathContext);

    boolean validateUnit(Unit unit);

    boolean validateUnit(UnitName unitName);

    boolean validateUnit(String unitName);
    
}
