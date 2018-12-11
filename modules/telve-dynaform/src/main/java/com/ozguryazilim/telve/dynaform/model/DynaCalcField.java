/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import com.ozguryazilim.telve.dynaform.calc.CalcResultRule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DynaForm'lar için calcultor field tanımı
 * @author Hakan Uygun
 */
public class DynaCalcField implements Serializable{
    
    private String id;
    private String label;
    private String group;
    private DynaCalcType method;
    private List<CalcResultRule> resultRules = new ArrayList<>();

    public DynaCalcField(String label, DynaCalcType method) {
        this.label = label;
        this.method = method;
        this.group = "default";
    }

    public DynaCalcField(String label, String group, DynaCalcType method) {
        this.label = label;
        this.group = group;
        this.method = method;
    }

    public DynaCalcField(String id, String label, String group, DynaCalcType method) {
        this.id = id;
        this.label = label;
        this.group = group;
        this.method = method;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public DynaCalcType getMethod() {
        return method;
    }

    public void setMethod(DynaCalcType method) {
        this.method = method;
    }

    public List<CalcResultRule> getResultRules() {
        return resultRules;
    }

    public void setResultRules(List<CalcResultRule> resultRules) {
        this.resultRules = resultRules;
    }
    
    /**
     * Hesap alanına yeni result rule ekler.
     * @param rule
     * @return 
     */
    public DynaCalcField addResultRule( CalcResultRule rule ){
        rule.setOwner(this);
        getResultRules().add(rule);
        return this;
    }
}
