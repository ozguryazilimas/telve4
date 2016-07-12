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
public class Quantity implements Serializable{
    
    private final BigDecimal amount;
    private final UnitName   unitName;

    public Quantity(BigDecimal amount, UnitName unitName) {
        this.amount = amount;
        this.unitName = unitName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UnitName getUnitName() {
        return unitName;
    }

    
    public static Quantity of( BigDecimal a,  UnitName un ){
        return new Quantity(a, un);
    }
    
    public static Quantity of( BigDecimal a,  String un ){
        return new Quantity(a, UnitName.of(un));
    }
    
    //TODO: JavaMoney ConvertBigDecimal utility'si kullanılabilir belki
    public static Quantity of( long a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( int a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( double a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( float a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( String a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( Long a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( Integer a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( Double a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( Float a,  String un ){
        return new Quantity( new BigDecimal(a), UnitName.of(un));
    }
    
    public static Quantity of( long a,  UnitName un ){
        return new Quantity( new BigDecimal(a), un);
    }
    
    public static Quantity of( int a,  UnitName un ){
        return new Quantity( new BigDecimal(a),un);
    }
    
    public static Quantity of( double a, UnitName un ){
        return new Quantity( new BigDecimal(a),un);
    }
    
    public static Quantity of( float a, UnitName un ){
        return new Quantity( new BigDecimal(a),un);
    }
    
    public static Quantity of( String a,UnitName un ){
        return new Quantity( new BigDecimal(a),un);
    }
    
    public static Quantity of( Long a, UnitName un ){
        return new Quantity( new BigDecimal(a), un);
    }
    
    public static Quantity of( Integer a, UnitName un ){
        return new Quantity( new BigDecimal(a), un);
    }
    
    public static Quantity of( Double a, UnitName un ){
        return new Quantity( new BigDecimal(a), un);
    }
    
    public static Quantity of( Float a, UnitName un ){
        return new Quantity( new BigDecimal(a), un);
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
        
        final Quantity other = (Quantity) obj;
        if (!Objects.equals(this.unitName, other.unitName)) {
            return false;
        }
        
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        
        return true;
    }
    



    public Quantity convert( UnitName unitName ) throws UnitException{
        
        //UnitName'ler aynı ise convert edecek bişi yok
        if( this.getUnitName().equals(unitName)) return this;
        
        if( !this.getUnitName().compatible(unitName) ) throw new UnitException("Uncompatible units : " + this.getUnitName() + " vs " + unitName);
        
        Dimension dimension = DimensionRegistery.getDimension(unitName.getDimension());
        
        if( dimension == null ) throw new UnitException("Unknown Dimension : " + this.getUnitName().getDimension());
        
        //0'da çarpan değer, 1'de bölen değer var.
        BigDecimal[] factors = dimension.getFactors(this.getUnitName(), unitName);
        
        //Biraz optimizasyon yapıp etkisiz eleman 1 gelmiş ise hesaba katmayabiliriz
        return of( this.getAmount().multiply(factors[0]).divide(factors[1], dimension.getMathContext()), unitName);
        
    }

    public Quantity add( BigDecimal amount ){
        return of( this.getAmount().add(amount), this.getUnitName());
    }
    
    
    public Quantity add( Quantity that ) throws UnitException{
        return add( that, this.getUnitName());
    }
    
    
    public Quantity add( Quantity that, UnitName unitName ) throws UnitException{
        Quantity a = this.convert(unitName);
        Quantity b = that.convert(unitName);
        return of( a.getAmount().add(b.getAmount()), unitName);
    }
    
    
    public Quantity subtract( BigDecimal amount ){
        return of( this.getAmount().subtract(amount), this.getUnitName());
    }
    
    public Quantity subtract( Quantity that ) throws UnitException{
        return subtract( that, this.getUnitName());
    }
    
    public Quantity subtract( Quantity that, UnitName unitName ) throws UnitException{
        Quantity a = this.convert(unitName);
        Quantity b = that.convert(unitName);
        return of( a.getAmount().subtract(b.getAmount()), unitName);
    }
    
    
    public Quantity multiply( BigDecimal amount ){
        return of( this.getAmount().multiply(amount), this.getUnitName());
    }
    
    public Quantity multiply( Quantity that ) throws UnitException{
        return multiply( that, this.getUnitName());
    }
    
    public Quantity multiply( Quantity that, UnitName unitName ) throws UnitException{
        Quantity a = this.convert(unitName);
        Quantity b = that.convert(unitName);
        return of( a.getAmount().multiply(b.getAmount()), unitName);
    }
    
    public Quantity divide( BigDecimal amount ){
        Dimension dimension = DimensionRegistery.getDimension(this.getUnitName().getDimension());
        return of( this.getAmount().divide(amount, dimension.getMathContext()), this.getUnitName());
    }
    
    public Quantity divide( Quantity that ) throws UnitException{
        return divide( that, this.getUnitName());
    }
    
    public Quantity divide( Quantity that, UnitName unitName ) throws UnitException{
        Quantity a = this.convert(unitName);
        Quantity b = that.convert(unitName);
        Dimension dimension = DimensionRegistery.getDimension(unitName.getDimension());
        return of( a.getAmount().divide(b.getAmount(), dimension.getMathContext()), unitName);
    }
    
}
