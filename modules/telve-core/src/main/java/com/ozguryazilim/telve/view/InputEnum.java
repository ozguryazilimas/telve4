package com.ozguryazilim.telve.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * Telve InpuEnum JSF bileşeni için control sınıfı.
 *
 * @author Hakan Uygun
 */
@FacesComponent("inputEnum")
public class InputEnum extends UINamingContainer {

    /**
     * Verilen Value eğer enum ise onun değer listesini döndürür.
     *
     * @return
     */
    public List<Object> getItems() {

        List<Object> result = new ArrayList<>();

        Class<?> valueType = getValueType();

        if( (Boolean)getAttributes().get("hasNullValue")){
            //Null değer girilebilmei için bir null ekliyoruz.
            result.add(null);
        }

        if (valueType.isEnum()) {
            result.addAll(Arrays.asList(valueType.getEnumConstants()));
        }

        return result;
    }

    /**
     * Girilen değerin tipini döndürür.
     */
    private Class<?> getValueType() {
        ValueExpression ve = getValueExpression("value");

        try {
            return ve.getType(FacesContext.getCurrentInstance().getELContext());
        } catch (Exception e) {
            //Do Nothing
        }
        return null;
    }


}
