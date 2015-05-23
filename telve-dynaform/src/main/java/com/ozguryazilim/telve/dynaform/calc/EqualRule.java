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
public class EqualRule<T> extends CalcRule<T>{

    private Long value;
    private T  compareKey;

    public EqualRule(Long value, T compareKey) {
        this.value = value;
        this.compareKey = compareKey;
    }

    public EqualRule(Long value, T compareKey, String valueGroup) {
        super(valueGroup);
        this.value = value;
        this.compareKey = compareKey;
    }
    
    
    
    @Override
    public Long calculateValue(DynaFieldBinder<T> binder) {
        
        if( compareKey.equals(binder.getValue()) ){
            return value;
        }
        
        return 0l;
    }
    
}
