package com.ozguryazilim.telve.unit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Boyut tanımı.
 * 
 * ZAMAN, UZUNLUK, AGIRLIK, HACIM v.b
 * 
 * içerisinde birim çevrim listesi bulunur.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractUnitSet implements UnitSet {
    
    private Map<UnitName,Unit> unitMap = new HashMap<>();
    private MathContext mathContext = MathContext.DECIMAL64; //new MathContext( MathContext.UNLIMITED, RoundingMode.HALF_UP);

    @Override
    public Map<UnitName, Unit> getUnitMap() {
        return unitMap;
    }

    public void setUnitMap(Map<UnitName, Unit> unitMap) {
        this.unitMap = unitMap;
    }

    @Override
    public MathContext getMathContext() {
        return mathContext;
    }

    @Override
    public void setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
    }
    
    @Override
    public final void addBaseUnit( String unitName ){
        UnitName un = new UnitName(getDimensionName(), unitName);
        Unit u = new Unit( un, new QuantitativeAmount(BigDecimal.ONE, un));
        unitMap.put(un, u);
    }
    
    @Override
    public final void addUnit( String unitName, QuantitativeAmount base ){
        UnitName un = new UnitName(getDimensionName(), unitName);
        Unit u = new Unit( un, base);
        unitMap.put(un, u);
    }
    
    @Override
    public final void addUnit( Unit unit ){
        unitMap.put(unit.getName(), unit);
    }
    
    @Override
    public Unit getUnit( UnitName unitName ){
        return unitMap.get(unitName);
    }
    
    @Override
    public boolean validateUnit( Unit unit ){
        return unitMap.containsKey(unit.getName());
    }
    
    @Override
    public boolean validateUnit( UnitName unitName ){
        return unitMap.containsKey(unitName);
    }
    
    @Override
    public boolean validateUnit( String unitName ){
        return unitMap.containsKey(UnitName.of(unitName));
    }
    
    @Override
    public void checkUnit( Unit unit ) throws UnitException{
        if( !validateUnit(unit)) throw new UnitException("Unknown Unit : " + unit );
    }
    
    @Override
    public void checkUnit( UnitName unitName ) throws UnitException{
        if( !validateUnit(unitName)) throw new UnitException("Unknown Unit : " + unitName );
    }
    
    @Override
    public List<Unit> getUnitChain( UnitName from ) throws UnitException{
        checkUnit(from);
        return getUnitChain(getUnit(from));
    }
    
    @Override
    public List<Unit> getUnitChain( Unit from ) throws UnitException{
        
        checkUnit(from);
        
        List<Unit> result = new ArrayList<>();
        
        result.add(from);
        
        //Eğer başlangıç noktası zaten baseUnit ise geri kalanı kontrol etmeye gerek yok
        if( from.isBaseUnit() ) return result;
        
        Unit f = getUnit(from.getBase().getUnitName());
        
        //FIXME: Burada exception mı fırlatmalı ne yapmalı? Tanımlarda bir sorun var demek çünkü?
        if( f == null ) {
            throw new UnitException("Unit Chain is Broken. Unit not found " + from.getBase().getUnitName());
        }
        
        while ( !f.isBaseUnit() ){
            result.add(f);
            f = getUnit(f.getBase().getUnitName());
        }
        //Son olarak BaseUnit'i ekleyelim
        result.add(f);
        
        return result;
    }
    
    /**
     * Bir birimden diğerine dönmek için gereken carpan ve bolen değerleri döndürür.
     * 
     * ilk değer çarpan, ikinci değer ise bölen değeridir.
     * 
     * @param from
     * @param to
     * @return 
     */
    @Override
    public BigDecimal[] getFactors( UnitName from, UnitName to ) throws UnitException{
        BigDecimal[] result = new BigDecimal[2];
        Arrays.fill( result, BigDecimal.ONE);
                
        List<Unit> fromChain = getUnitChain(from);
        List<Unit> toChain = getUnitChain(to);
        
        for( Unit f : fromChain){
            
            if( !toChain.contains(f)){
                result[0] = result[0].multiply(f.getBase().getAmount());
            } else {
                //Keşimi bulduk şimdi to üzerinde geri yürüyeceğiz ve bölen değerimizi bulacağız
                for( Unit r : toChain ){
                    if( !f.equals(r) ){
                        result[1] = result[1].multiply(r.getBase().getAmount());
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Geriye ilgili UnitSet'ın sahip olduğu birim isimlerini döndürür
     * @return 
     */
    @Override
    public List<UnitName> getUnitNames(){
        return new ArrayList( unitMap.keySet() );
    }
}
