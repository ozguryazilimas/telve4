/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import com.ozguryazilim.telve.dynaform.calc.CalcRule;
import java.util.Date;

/**
 *
 * @author Hakan Uygun
 */
public class DynaDateField extends DynaField<Date>{

    public DynaDateField(String label) {
        super(label);
    }

    public DynaDateField(String label, String placeholder) {
        super(label, placeholder);
    }

    public DynaDateField(String id, String label, String placeholder) {
        super(id, label, placeholder);
    }

    public DynaDateField(String id, String label, String placeholder, Date defaultValue) {
        super(id, label, placeholder, defaultValue);
    }

    public DynaDateField(String id, String label, String placeholder, String permission, Date defaultValue) {
        super(id, label, placeholder, permission, defaultValue);
    }

    public DynaDateField(String label, CalcRule<Date>... rules) {
        super(label, rules);
    }

    
    
    @Override
    public Class<Date> getValueClass() {
        return Date.class;
    }
    
}
