package com.ozguryazilim.telve.config;

/**
 * Option key bilgilerini tutar.
 *
 * @author sinan.yumak
 *
 */
public interface OptionKey {
    	/**
	 * Optionın değerini döndürür.
	 * @return option value
	 */
	String getValue();
	
	/**
	 * Option için varsayılan değeri döndürür.
	 * @return default option value
	 */
	String getDefaultValue();
}
