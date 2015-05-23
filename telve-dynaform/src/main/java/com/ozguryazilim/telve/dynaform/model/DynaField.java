/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import com.ozguryazilim.telve.dynaform.calc.CalcRule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DynaForm için temel Field model sınıfı.
 * 
 * Bu sınıf kendi veri türleri için miras alınacak
 * 
 * @author Hakan Uygun 
 */
public abstract class DynaField<T> implements Serializable{
    
    private String id;
    private String label;
    private String placeholder;
    private String permission;
    private Boolean required = Boolean.FALSE;
    private Boolean readonly = Boolean.FALSE;
    private T defaultValue;
    private List<CalcRule<T>> rules = new ArrayList<>();

    public DynaField(String label) {
        this.label = label;
    }

    public DynaField(String label, String placeholder) {
        this.label = label;
        this.placeholder = placeholder;
    }

    public DynaField(String id, String label, String placeholder) {
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
    }

    public DynaField(String id, String label, String placeholder, T defaultValue) {
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.defaultValue = defaultValue;
    }

    public DynaField(String id, String label, String placeholder, String permission, T defaultValue) {
        this.id = id;
        this.label = label;
        this.placeholder = placeholder;
        this.permission = permission;
        this.defaultValue = defaultValue;
    }

    public DynaField(String label, CalcRule<T>... rules) {
        this.label = label;
        this.rules = Arrays.asList(rules);
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

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<CalcRule<T>> getRules() {
        return rules;
    }

    public void setRules(List<CalcRule<T>> rules) {
        this.rules = rules;
    }
    
    public abstract Class<T> getValueClass();
    
    
}
