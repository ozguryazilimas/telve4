package com.ozguryazilim.telve.dynaform.calc;

import com.ozguryazilim.telve.dynaform.binder.DynaFieldBinder;
import java.io.Serializable;

/**
 *
 * @author Hakan Uygun
 */
public abstract class CalcRule<T> implements Serializable{
    
    private String valueGroup;

    public CalcRule() {
        valueGroup = "default";
    }
    
    public CalcRule(String valueGroup) {
        this.valueGroup = valueGroup;
    }

    public Long calculate(DynaFieldBinder<T> binder ){
        return calculate(binder, "default");
    }
    
    public Long calculate(DynaFieldBinder<T> binder, String valueGroup ){
        if( valueGroup.equals(this.valueGroup)){
            return calculateValue(binder);
        } else {
            return 0l;
        }
    }

    public abstract Long calculateValue(DynaFieldBinder<T> binder );
    
    public String getValueGroup() {
        return valueGroup;
    }

    public void setValueGroup(String valueGroup) {
        this.valueGroup = valueGroup;
    }
}
