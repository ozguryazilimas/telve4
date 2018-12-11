/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.calc;

import com.ozguryazilim.telve.dynaform.model.DynaCalcField;
import com.ozguryazilim.telve.dynaform.model.DynaCalcFieldValue;
import java.io.Serializable;
import java.util.Map;

/**
 * Hesaplama sonuç kararı verecek rule tabanı
 * @author Hakan Uygun
 */
public class CalcResultRule implements Serializable{
    
    private DynaCalcField owner;
    private String value;
    private String result;
    private String style;
    private CalcResultCheckType checkType;

    public CalcResultRule(String value, String result, CalcResultCheckType checkType) {
        this.value = value;
        this.result = result;
        this.checkType = checkType;
    }

    public CalcResultRule(String value, String result, String style, CalcResultCheckType checkType) {
        this.value = value;
        this.result = result;
        this.style = style;
        this.checkType = checkType;
    }

    public CalcResultRule(DynaCalcField owner, String value, String result, String style, CalcResultCheckType checkType) {
        this.owner = owner;
        this.value = value;
        this.result = result;
        this.style = style;
        this.checkType = checkType;
    }

    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public CalcResultCheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CalcResultCheckType checkType) {
        this.checkType = checkType;
    }

    public DynaCalcField getOwner() {
        return owner;
    }

    public void setOwner(DynaCalcField owner) {
        this.owner = owner;
    }

    
    
    /**
     * Kontrol edilecek değere bakarak kuralın geçerli olup olmadığın döner.
     * @param value
     * @param checkValue
     * @return 
     */
    protected Boolean ruleMatch( Long value, Long checkValue ){
        
        switch ( checkType ){
            case Eq : return value.compareTo(checkValue) == 0;
            case Ne : return value.compareTo(checkValue) != 0;
            case Gt : return value.compareTo(checkValue) > 0;
            case Ge : return value.compareTo(checkValue) >= 0;
            case Lt : return value.compareTo(checkValue) < 0;
            case Le : return value.compareTo(checkValue) <= 0;
            default: return false;
        }
    }

    /**
     * CalcField'ın değerini kontrol ederek result hesaplaması yapar.
     * @param fields 
     */
    public void calcResult(Map<String, DynaCalcFieldValue> fields ){

        DynaCalcFieldValue v = fields.get(owner.getId());
        if( v == null ){
            //Bu kural için hesaplanmış bir sonuç yok.
            return;
        }
        
        Long calcValue = v.getValue();
        Long checkValue = getCheckValue(fields);
        
        if( ruleMatch(calcValue, checkValue)){
            v.setResult(result);
            v.setStyle(style);
        }
        
    }
    
    /**
     * Geriye kontrol için istenen değeri döndürür.
     * @param fields
     * @return 
     */
    protected Long getCheckValue( Map<String, DynaCalcFieldValue> fields ){
        
        DynaCalcFieldValue v = fields.get(value);
        if( v != null ){
            //Demek ki değerimiz bir field mış.
            return v.getValue();
        } 
        
        //Demek ki field değil değermiş
        return Long.parseLong(value);
    }
    
    
}
