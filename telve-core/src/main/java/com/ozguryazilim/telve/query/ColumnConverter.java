package com.ozguryazilim.telve.query;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 * 
 * @author Hakan Uygun
 */
@FacesConverter("ColumnConverter")
public class ColumnConverter implements javax.faces.convert.Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		return ColumnStore.instance().get(arg2);
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		if( arg2 instanceof Column ){
			return  ColumnStore.instance().put((Column)arg2);
		}
		
		return null;
	}

}
