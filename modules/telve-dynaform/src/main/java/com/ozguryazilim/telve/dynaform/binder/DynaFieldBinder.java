package com.ozguryazilim.telve.dynaform.binder;

import com.ozguryazilim.telve.dynaform.model.DynaField;
import java.io.Serializable;
import java.util.Map;

/**
 * Data ile DynaFieldları bnde eder. 
 * 
 * UI bu sınıfları kullanır.
 * 
 * @author Hakan Uygun
 */
public class DynaFieldBinder<T> implements Serializable{
    
    private Map<String,Object> valueMap;
    private DynaField<T> field;

    public DynaFieldBinder(DynaField<T> field, Map<String, Object> valueMap ) {
        this.valueMap = valueMap;
        this.field = field;
    }
    
    public T getValue(){
        T val = (T) valueMap.get(field.getId());
        if( val == null ){
            val = field.getDefaultValue();
        }
        
        return val;
    }
    
    public void setValue( T value ){
        valueMap.put(field.getId(), value);
    }

    public DynaField<T> getField() {
        return field;
    }
    
    
}
