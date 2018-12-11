package com.ozguryazilim.telve.query;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author oyas
 *
 */
@FacesConverter("NumberConverter")
public class NumberConverter implements Converter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6999571728694102808L;

	public Number getAsObject(FacesContext context, UIComponent component, String value) {
		DecimalFormat df = generateDecimalFormat(component);	
		
		try {
			return df.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		DecimalFormat df = generateDecimalFormat(component);
		
		if(value instanceof Number){
			return df.format(value);
		}

		return null;
	}

	private DecimalFormat generateDecimalFormat(UIComponent component){
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		
		Character decimalSeperator = (component.getAttributes().get("decimalSeperator").toString()).charAt(0);
		Character thousandSeperator= (component.getAttributes().get("thousandSeperator").toString()).charAt(0);
		
		Integer maxFraction = Integer.parseInt(component.getAttributes().get("maxFraction").toString());
		Integer minFraction = Integer.parseInt(component.getAttributes().get("minFraction").toString());
		
		symbols.setDecimalSeparator(decimalSeperator);
		symbols.setGroupingSeparator(thousandSeperator);
		df.setMaximumFractionDigits(maxFraction);
		df.setMinimumFractionDigits(minFraction);
		
		df.setDecimalFormatSymbols(symbols);
		
		return df;
		
	}

}
