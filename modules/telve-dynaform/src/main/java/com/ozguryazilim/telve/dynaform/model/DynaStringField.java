/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import com.ozguryazilim.telve.dynaform.calc.CalcRule;

/**
 * String/Text alanlar için 
 * @author Hakan Uygun
 */
public class DynaStringField extends DynaField<String>{

    public DynaStringField(String label) {
        super(label);
    }

    public DynaStringField(String label, CalcRule<String>... rules) {
        super(label, rules);
    }

    
    public DynaStringField(String label, String placeholder) {
        super(label, placeholder);
    }

    public DynaStringField(String id, String label, String placeholder) {
        super(id, label, placeholder);
    }

    public DynaStringField(String id, String label, String placeholder, String defaultValue) {
        super(id, label, placeholder, defaultValue);
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

}
