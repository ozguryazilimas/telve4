/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Birim Tanımı
 * 
 * Model olarak kalmasını istediğimiz için artikmetik işlemleri Quantities'e aldık.
 * Çünkü çevrim işlemleri için Başka başka şeylere ihtiyaç duyoruz.
 * 
 * @author Hakan Uygun 
 */
public class QuantitativeAmount implements Serializable{
    
    private final BigDecimal amount;
    private final UnitName   unitName;

    public QuantitativeAmount(BigDecimal amount, UnitName unitName) {
        this.amount = amount;
        this.unitName = unitName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UnitName getUnitName() {
        return unitName;
    }

    
    public static QuantitativeAmount of( BigDecimal a,  UnitName un ){
        return new QuantitativeAmount(a, un);
    }
    
    public static QuantitativeAmount of( BigDecimal a,  String un ){
        return new QuantitativeAmount(a, UnitName.of(un));
    }
    
    //TODO: JavaMoney ConvertBigDecimal utility'si kullanılabilir belki
    public static QuantitativeAmount of( long a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( int a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( double a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( float a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( String a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( Long a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( Integer a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( Double a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( Float a,  String un ){
        return new QuantitativeAmount( new BigDecimal(a), UnitName.of(un));
    }
    
    public static QuantitativeAmount of( long a,  UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a), un);
    }
    
    public static QuantitativeAmount of( int a,  UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a),un);
    }
    
    public static QuantitativeAmount of( double a, UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a),un);
    }
    
    public static QuantitativeAmount of( float a, UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a),un);
    }
    
    public static QuantitativeAmount of( String a,UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a),un);
    }
    
    public static QuantitativeAmount of( Long a, UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a), un);
    }
    
    public static QuantitativeAmount of( Integer a, UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a), un);
    }
    
    public static QuantitativeAmount of( Double a, UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a), un);
    }
    
    public static QuantitativeAmount of( Float a, UnitName un ){
        return new QuantitativeAmount( new BigDecimal(a), un);
    }
    
    @Override
    public String toString() {
        return unitName.toString() + '(' + amount.toString() + ')';
    }
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.amount);
        hash = 13 * hash + Objects.hashCode(this.unitName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final QuantitativeAmount other = (QuantitativeAmount) obj;
        if (!Objects.equals(this.unitName, other.unitName)) {
            return false;
        }
        
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        
        return true;
    }
    



    public QuantitativeAmount convert( UnitName unitName ) throws UnitException{
        
        //UnitName'ler aynı ise convert edecek bişi yok
        if( this.getUnitName().equals(unitName)) return this;
        
        if( !this.getUnitName().compatible(unitName) ) throw new UnitException("Uncompatible units : " + this.getUnitName() + " vs " + unitName);
        
        UnitSet us = UnitSetRegistery.getUnitSet(unitName.getUnitSet());
        
        if( us == null ) throw new UnitException("Unknown Unit Set : " + this.getUnitName().getUnitSet());
        
        //0'da çarpan değer, 1'de bölen değer var.
        BigDecimal[] factors = us.getFactors(this.getUnitName(), unitName);
        
        //Biraz optimizasyon yapıp etkisiz eleman 1 gelmiş ise hesaba katmayabiliriz
        return of( this.getAmount().multiply(factors[0]).divide(factors[1], us.getMathContext()), unitName);
        
    }

    public QuantitativeAmount add( BigDecimal amount ){
        return of( this.getAmount().add(amount), this.getUnitName());
    }
    
    
    public QuantitativeAmount add( QuantitativeAmount that ) throws UnitException{
        return add( that, this.getUnitName());
    }
    
    
    public QuantitativeAmount add( QuantitativeAmount that, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = this.convert(unitName);
        QuantitativeAmount b = that.convert(unitName);
        return of( a.getAmount().add(b.getAmount()), unitName);
    }
    
    
    public QuantitativeAmount subtract( BigDecimal amount ){
        return of( this.getAmount().subtract(amount), this.getUnitName());
    }
    
    public QuantitativeAmount subtract( QuantitativeAmount that ) throws UnitException{
        return subtract( that, this.getUnitName());
    }
    
    public QuantitativeAmount subtract( QuantitativeAmount that, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = this.convert(unitName);
        QuantitativeAmount b = that.convert(unitName);
        return of( a.getAmount().subtract(b.getAmount()), unitName);
    }
    
    
    public QuantitativeAmount multiply( BigDecimal amount ){
        return of( this.getAmount().multiply(amount), this.getUnitName());
    }
    
    public QuantitativeAmount multiply( QuantitativeAmount that ) throws UnitException{
        return multiply( that, this.getUnitName());
    }
    
    public QuantitativeAmount multiply( QuantitativeAmount that, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = this.convert(unitName);
        QuantitativeAmount b = that.convert(unitName);
        return of( a.getAmount().multiply(b.getAmount()), unitName);
    }
    
    public QuantitativeAmount divide( BigDecimal amount ){
        UnitSet us = UnitSetRegistery.getUnitSet(this.getUnitName().getUnitSet());
        return of( this.getAmount().divide(amount, us.getMathContext()), this.getUnitName());
    }
    
    public QuantitativeAmount divide( QuantitativeAmount that ) throws UnitException{
        return divide( that, this.getUnitName());
    }
    
    public QuantitativeAmount divide( QuantitativeAmount that, UnitName unitName ) throws UnitException{
        QuantitativeAmount a = this.convert(unitName);
        QuantitativeAmount b = that.convert(unitName);
        UnitSet us = UnitSetRegistery.getUnitSet(unitName.getUnitSet());
        return of( a.getAmount().divide(b.getAmount(), us.getMathContext()), unitName);
    }
    
}
