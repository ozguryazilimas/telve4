/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * Enum Tipler için filtre.
 * 
 * JSF EnumConverter genericlerle doğru çalışmadığı için stringValue değeri üzerinden işlem yapılıyor.
 * 
 * @author Hakan Uygun
 * @param <E> Filtrenin uygulanacağı Entity sınıfı
 * @param <T> Filtre olarak kullanılacak enum sınıfı
 */
public class EnumFilter<E, T extends Enum<T> > extends Filter<E, T>{

    /**
     * Enum dil desteği için kullanılacak olan prefix
     */
    private String keyPrefix;
    private T[] enums;
    
    /**
     * Enum tipler için filtre oluşturur.
     * 
     * @param attribute Hangi alanın filtreye tabi tutulacağı
     * @param defaultValue varsayılan olarak hangi değer olacak?
     * @param label filtre labelı
     * @param itemLabel enum itemlar için label prefix
     */
    public EnumFilter(SingularAttribute<? super E, T> attribute, T defaultValue, String label, String itemLabel ) {
        
        super(attribute, label);
        
        keyPrefix = itemLabel;
        enums = defaultValue.getDeclaringClass().getEnumConstants();
        
        setOperands(Operands.getEnumOperands());
        setOperand(FilterOperand.Equal);
        setValue(defaultValue);
    }

    /**
     * Sunumda hangi enum değerlerinin kullanılacağı parametre olarak verilebilir.
     * @param attribute
     * @param defaultValue
     * @param filterValues
     * @param label
     * @param itemLabel 
     */
    public EnumFilter(SingularAttribute<? super E, T> attribute, T defaultValue, T[] filterValues, String label, String itemLabel ) {
        super(attribute, label);
        
        keyPrefix = itemLabel;
        enums = filterValues;
        
        setOperands(Operands.getEnumOperands());
        setOperand(FilterOperand.Equal);
        setValue(defaultValue);
    }
    
    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        if (getValue() != null) {
            switch (getOperand()) {
                case Equal:
                    criteria.eq(getAttribute(), getValue());
                    break;
                case NotEqual:
                    criteria.notEq(getAttribute(), getValue());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getTemplate() {
        return "enumFilter";
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public T[] getEnums() {
        return enums;
    }

    public void setEnums(T[] enums) {
        this.enums = enums;
    }
    
    /**
     * Geriye enum'un String değerini döndürür.
     * 
     * @return 
     */
    public String getStringValue(){
        return getValue().name();
    }
    
    /**
     * Verilen stringi enum'a çevirir.
     * 
     * @param val 
     */
    public void setStringValue( String val ){
        setValue( T.valueOf( getValue().getDeclaringClass() , val));
    }

    @Override
    public String serialize() {
        return Joiner.on("::").join(getOperand(), getStringValue());
    }

    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        setStringValue(ls.get(1));
    }
    
}
