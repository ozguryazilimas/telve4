package com.ozguryazilim.telve.dynaform.model;

/**
 *
 * @author haky
 */
public class DynaLongField extends DynaField<Long>{

    public DynaLongField(String id, String label, String placeholder) {
        super(id, label, placeholder);
    }

    public DynaLongField(String id, String label, String placeholder, Long defaultValue) {
        super(id, label, placeholder, defaultValue);
    }

    public DynaLongField(String id, String label, String placeholder, String permission, Long defaultValue) {
        super(id, label, placeholder, permission, defaultValue);
    }

    @Override
    public Class<Long> getValueClass() {
        return Long.class;
    }
    
    
}
