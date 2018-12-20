package com.ozguryazilim.telve.dynaform.binder;

import com.ozguryazilim.telve.dynaform.model.DynaCalcField;
import com.ozguryazilim.telve.dynaform.model.DynaCalcFieldValue;
import java.util.Map;

/**
 * UI için DynaCalcField değerlerini bind eder.
 * @author Hakan Uygun
 */
public class DynaCalcFieldBinder {
    
    private Map<String,DynaCalcFieldValue> valueMap;
    private DynaCalcField field;

    public DynaCalcFieldBinder(DynaCalcField field, Map<String, DynaCalcFieldValue> valueMap ) {
        this.valueMap = valueMap;
        this.field = field;
    }
    
    
    public Long getValue(){
        DynaCalcFieldValue v = valueMap.get(field.getId());
        return  v == null ? 0l : v.getValue();
    }
    
    public String getResult(){
        DynaCalcFieldValue v = valueMap.get(field.getId());
        return  v == null ? "" : v.getResult();
    }

    
    public String getStyle(){
        DynaCalcFieldValue v = valueMap.get(field.getId());
        return  v == null ? "" : v.getStyle();
    }
    
}
