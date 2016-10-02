/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import java.math.BigDecimal;

/**
 * Birim ve Miktar işlemleri için temel fonksiyonlar
 * 
 * @author Hakan Uygun
 */
public class Quantities {
   
    
    /**
     * Verilen miktar ve birim ile QuantitativeAmount oluşturur.
     * 
     * @param amount
     * @param unit
     * @return 
     */
    public static QuantitativeAmount of( BigDecimal amount, Unit unit ){
        return new QuantitativeAmount(amount, unit.getName());
    }
    
    /**
     * Verilen miktar ve birim ile QuantitativeAmount oluşturur.
     * 
     * @param amount
     * @param unitName 
     * @return 
     */
    public static QuantitativeAmount of( BigDecimal amount, UnitName unitName ){
        return new QuantitativeAmount(amount, unitName );
    }
    
    /**
     * Verilen miktar ve birim ile QuantitativeAmount oluşturur.
     * 
     * @param amount
     * @param unitName 
     * @return 
     */
    public static QuantitativeAmount of( BigDecimal amount, String unitName ){
        return new QuantitativeAmount(amount, UnitName.of(unitName) );
    }

    public static QuantitativeAmount convert( QuantitativeAmount quantity, UnitName unitName ) throws UnitException{
        
        //UnitName'ler aynı ise convert edecek bişi yok
        if( quantity.getUnitName().equals(unitName)) return quantity;
        
        if( !quantity.getUnitName().compatible(unitName) ) throw new UnitException("Uncompatible units : " + quantity.getUnitName() + " vs " + unitName);
        
        UnitSet us = UnitSetRegistery.getUnitSet(unitName.getUnitSet());
        
        if( us == null ) throw new UnitException("Unknown UnitSet : " + quantity.getUnitName().getUnitSet());
        
        //0'da çarpan değer, 1'de bölen değer var.
        BigDecimal[] factors = us.getFactors(quantity.getUnitName(), unitName);
        
        //Biraz optimizasyon yapıp etkisiz eleman 1 gelmiş ise hesaba katmayabiliriz
        return of( quantity.getAmount().multiply(factors[0]).divide(factors[1], us.getMathContext()), unitName);
        
    }

    /**
     * Verilen değeri aynı birim olmak üzere ekler
     * 
     * @param base
     * @param amount
     * @return 
     */
    public static QuantitativeAmount add( QuantitativeAmount x, BigDecimal amount ){
        return of( x.getAmount().add(amount), x.getUnitName());
    }
    
    /**
     * Verilen iki değeri toplar.
     * Birincinin tipinde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @return base biriminde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount add( QuantitativeAmount x, QuantitativeAmount y ) throws UnitException{
        return add( x, y, x.getUnitName());
    }
    
    /**
     * Verilen iki değeri toplar ve verilen birimde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @param unitName
     * @return unitName tipinde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount add( QuantitativeAmount x, QuantitativeAmount y, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = convert(x, unitName);
        QuantitativeAmount b = convert(y, unitName);
        return of( a.getAmount().add(b.getAmount()), unitName);
    }
    
    
    /**
     * Verilen değeri aynı birim olmak üzere çıkarır
     * 
     * @param base
     * @param amount
     * @return 
     */
    public static QuantitativeAmount subtract( QuantitativeAmount x, BigDecimal amount ){
        return of( x.getAmount().subtract(amount), x.getUnitName());
    }
    
    /**
     * Verilen iki değeri çıkarır.
     * Birincinin tipinde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @return base biriminde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount subtract( QuantitativeAmount x, QuantitativeAmount y ) throws UnitException{
        return subtract( x, y, x.getUnitName());
    }
    
    /**
     * Verilen iki değeri çıkarır ve verilen birimde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @param unitName
     * @return unitName tipinde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount subtract( QuantitativeAmount x, QuantitativeAmount y, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = convert(x, unitName);
        QuantitativeAmount b = convert(y, unitName);
        return of( a.getAmount().subtract(b.getAmount()), unitName);
    }
    
    
    /**
     * Verilen değeri aynı birim olmak üzere çarpar
     * 
     * @param base
     * @param amount
     * @return 
     */
    public static QuantitativeAmount multiply( QuantitativeAmount x, BigDecimal amount ){
        return of( x.getAmount().multiply(amount), x.getUnitName());
    }
    
    /**
     * Verilen iki değeri çarpar.
     * Birincinin tipinde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @return base biriminde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount multiply( QuantitativeAmount x, QuantitativeAmount y ) throws UnitException{
        return multiply( x, y, x.getUnitName());
    }
    
    /**
     * Verilen iki değeri çarpar ve verilen birimde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @param unitName
     * @return unitName tipinde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount multiply( QuantitativeAmount x, QuantitativeAmount y, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = convert(x, unitName);
        QuantitativeAmount b = convert(y, unitName);
        return of( a.getAmount().multiply(b.getAmount()), unitName);
    }
    
    /**
     * Verilen değeri aynı birim olmak üzere çarpar
     * 
     * @param base
     * @param amount
     * @return 
     */
    public static QuantitativeAmount divide( QuantitativeAmount base, BigDecimal amount ){
        UnitSet us = UnitSetRegistery.getUnitSet(base.getUnitName().getUnitSet());
        return of( base.getAmount().divide(amount, us.getMathContext()), base.getUnitName());
    }
    
    /**
     * Verilen iki değeri çarpar.
     * Birincinin tipinde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @return base biriminde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount divide( QuantitativeAmount x, QuantitativeAmount y ) throws UnitException{
        return divide( x, y, x.getUnitName());
    }
    
    /**
     * Verilen iki değeri çarpar ve verilen birimde geri dönüş yapar
     * 
     * @param base
     * @param added
     * @param unitName
     * @return unitName tipinde toplam
     * @throws UnitException 
     */
    public static QuantitativeAmount divide( QuantitativeAmount x, QuantitativeAmount y, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = convert(x, unitName);
        QuantitativeAmount b = convert(y, unitName);
        UnitSet us = UnitSetRegistery.getUnitSet(unitName.getUnitSet());
        return of( a.getAmount().divide(b.getAmount(), us.getMathContext()), unitName);
    }
}
