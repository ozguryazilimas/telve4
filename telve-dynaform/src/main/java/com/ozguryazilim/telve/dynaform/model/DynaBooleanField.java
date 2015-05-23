/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import com.ozguryazilim.telve.dynaform.calc.CalcRule;

/**
 *
 * @author Hakan Uygun
 */
public class DynaBooleanField extends DynaField<Boolean>{

    public DynaBooleanField(String label) {
        super(label);
    }

    public DynaBooleanField(String label, String placeholder) {
        super(label, placeholder);
    }

    public DynaBooleanField(String id, String label, String placeholder) {
        super(id, label, placeholder);
    }

    public DynaBooleanField(String id, String label, String placeholder, Boolean defaultValue) {
        super(id, label, placeholder, defaultValue);
    }

    public DynaBooleanField(String id, String label, String placeholder, String permission, Boolean defaultValue) {
        super(id, label, placeholder, permission, defaultValue);
    }

    public DynaBooleanField(String label, CalcRule<Boolean>... rules) {
        super(label, rules);
    }

    
    
    @Override
    public Class<Boolean> getValueClass() {
        return Boolean.class;
    }
    
}
