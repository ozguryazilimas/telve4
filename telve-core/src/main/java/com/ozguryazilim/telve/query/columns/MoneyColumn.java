/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import com.google.common.base.Strings;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Currency;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author oyas
 */
public class MoneyColumn<E> extends Column<E>{

    private Attribute<? super E, Currency> currencyColumn;
    
    public MoneyColumn(Attribute<? super E, ?> attribute, Attribute<? super E, Currency> currency, String labelKey) {
        super(attribute, labelKey);
        this.currencyColumn = currency;
    }

    @Override
    public String getTemplate() {
        return "moneyColumn";
    }

    public Attribute<? super E, Currency> getCurrencyColumn() {
        return currencyColumn;
    }

    public void setCurrencyColumn(Attribute<? super E, Currency> currencyColumn) {
        this.currencyColumn = currencyColumn;
    }
    
    public String getCurrencyName(){
        return currencyColumn.getName();
    }

    @Override
    public void export(E e, Writer doc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        //doc.write("\"");
        String val = BeanUtils.getProperty(e, getName());
        if( !Strings.isNullOrEmpty(val)){
            doc.write( val );
        }
        //doc.write("\"");
    }
    
    
    
}
