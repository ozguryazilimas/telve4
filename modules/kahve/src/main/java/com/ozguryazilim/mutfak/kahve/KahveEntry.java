package com.ozguryazilim.mutfak.kahve;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Kahve DB içinde tutulacak olan veri modeli
 * 
 * @author Hakan Uygun
 */
public class KahveEntry implements Serializable{
   
    private String value; 

    public KahveEntry() {
    }

    
    public KahveEntry( String value) {
        this.value = value;
    }
    
    public KahveEntry( Integer value) {
        setAsInteger(value);
    }
    
    public KahveEntry( Long value) {
        setAsLong(value);
    }
    
    public KahveEntry( Date value) {
        setAsDate(value);
    }
    
    public KahveEntry( Boolean value) {
        setAsBoolean(value);
    }
    
    public KahveEntry( BigDecimal value) {
        setAsBigDecimal(value);
    }

    public KahveEntry( Enum value) {
        //Eğer gelen enum KahveKey tipindeyse defaultValuesunu kullanıyoruz.
        if( value instanceof KahveKey ){
            this.value = ((KahveKey)value).getDefaultValue();
        } else {
            setAsEnum(value);
        }
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getAsString() {
        return value;
    }

    public void setAsString(String val) {
        value = val;
    }

    public Integer getAsInteger() {
        return Integer.parseInt(value);
    }

    public final void setAsInteger(Integer val) {
        value = val.toString();
    }
    
    public Long getAsLong() {
        return Long.parseLong(value);
    }

    public final void setAsLong(Long val) {
        value = val.toString();
    }

    @SuppressWarnings("deprecation")
    public Date getAsDate() {
        return new Date(value);
    }

    public final void setAsDate(Date val) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        value = sdf.format(val);
    }

    public Boolean getAsBoolean() {
        return Boolean.parseBoolean(value);
    }

    public final void setAsBoolean(Boolean bool) {
        value = bool.toString();
    }

    public BigDecimal getAsBigDecimal() {
        return new BigDecimal(value);
    }

    public final void setAsBigDecimal(BigDecimal aValue) {
        //TODO:scale i 2 mi almalı parametreye mi bağlamalı?
        value = aValue.setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * İstenen enuma çevirme işlemlerini yapar.
     *
     * @param <T> hedef enum tipi
     * @param enumClazz, hedef enum tipi
     * @return enum, çevirimi yapılmış enum.
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum> T getAsEnum(Class<T> enumClazz) {

        T o = null;
        try {
            o = (T) Enum.valueOf(enumClazz, value);
        } catch (Exception e) {
			//verilen değer enumeration içerisinde olmadığından
            //null dönecektir.
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public final void setAsEnum(Enum enumVal) {
        value = enumVal.name();
    }

    //TODO: DateTime, Time, v.b. alanlar yazılacak.
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KahveEntry other = (KahveEntry) obj;
        
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "KahveEntry{ value=" + value + '}';
    }
    
    
    
}
