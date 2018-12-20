package com.ozguryazilim.telve.dynaform.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author haky
 */
public class DynaStringListField extends DynaField<String>{

    private List<String> valueList;
    private String labelPrefix;
    
    public DynaStringListField(String id, String label, String placeholder, List<String> valueList) {
        super(id, label, placeholder);
        this.valueList = valueList;
    }
    
    public DynaStringListField(String id, String label, String placeholder, List<String> valueList, String prefix) {
        super(id, label, placeholder);
        this.valueList = valueList;
        this.labelPrefix = prefix;
    }
    
    public DynaStringListField(String id, String label, String placeholder, List<String> valueList, String defaultValue, String prefix) {
        super(id, label, placeholder, defaultValue);
        this.valueList = valueList;
        this.labelPrefix = prefix;
    }
    
    /**
     * Virgüllerle ayrılmış şekilde liste verilebilir
     * @param id
     * @param label
     * @param placeholder
     * @param valueList 
     * @param prefix 
     */
    public DynaStringListField(String id, String label, String placeholder, String valueList, String prefix) {
        super(id, label, placeholder);
        
        List<String> ls = Arrays.asList(valueList.split(",")); 
        this.valueList = ls;
        this.labelPrefix = prefix;
    }
    
    public DynaStringListField(String id, String label, String placeholder, String valueList, String defaultValue, String prefix) {
        super(id, label, placeholder, defaultValue);
        
        List<String> ls = Arrays.asList(valueList.split(",")); 
        this.valueList = ls;
        this.labelPrefix = prefix;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }

    public String getLabelPrefix() {
        return labelPrefix;
    }

    public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }
    
}
