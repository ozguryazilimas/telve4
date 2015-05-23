/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import com.ozguryazilim.telve.dynaform.calc.CalcRule;
import java.math.BigDecimal;

/**
 * 
 * @author Hakan Uygun
 */
public class DynaBigDecimalField extends DynaField<BigDecimal>{

    public DynaBigDecimalField(String label) {
        super(label);
    }

    public DynaBigDecimalField(String label, String placeholder) {
        super(label, placeholder);
    }

    public DynaBigDecimalField(String id, String label, String placeholder) {
        super(id, label, placeholder);
    }

    public DynaBigDecimalField(String id, String label, String placeholder, BigDecimal defaultValue) {
        super(id, label, placeholder, defaultValue);
    }

    public DynaBigDecimalField(String id, String label, String placeholder, String permission, BigDecimal defaultValue) {
        super(id, label, placeholder, permission, defaultValue);
    }

    public DynaBigDecimalField(String label, CalcRule<BigDecimal>... rules) {
        super(label, rules);
    }

    
    
    @Override
    public Class<BigDecimal> getValueClass() {
        return BigDecimal.class;
    }
    
}
