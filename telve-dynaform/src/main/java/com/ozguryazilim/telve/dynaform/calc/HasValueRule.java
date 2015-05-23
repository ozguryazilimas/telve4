/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.calc;

import com.ozguryazilim.telve.dynaform.binder.DynaFieldBinder;

/**
 *
 * @author haky
 */
public class HasValueRule<T> extends CalcRule<T>{

    private Long value;

    public HasValueRule(Long value) {
        this.value = value;
    }

    public HasValueRule(Long value, String valueGroup) {
        super(valueGroup);
        this.value = value;
    }
    
    @Override
    public Long calculateValue(DynaFieldBinder<T> binder) {
        if( binder.getValue() != null ) {
            return value;
        } 
        
        return 0l;
    }
    
}
