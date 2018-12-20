package com.ozguryazilim.telve.view;

import com.ozguryazilim.telve.utils.RomanNumeral;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Integer değerleri roman rakamı olarak convert eder.
 * 
 * @author Hakan Uygun
 */
@FacesConverter("romanNumberConverter")
public class RomanNumberConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        RomanNumeral instance = new RomanNumeral(string);
        return instance.toInt();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if( o instanceof Integer){
            RomanNumeral instance = new RomanNumeral((Integer)0);
            return instance.toString();
        } 
        
        return "";
    }
    
}
