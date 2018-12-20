package com.ozguryazilim.mutfak.kahve;

/**
 * KahveEntry'leri için default değerleri olan key tanımlama arayüzü.
 * 
 * @author Hakan Uygun
 */
public interface KahveKey {

    /**
     * Key değerini döndürür
     *
     * @return key
     */
    String getKey();

    /**
     * Key için varsayılan değeri döndürür.
     *
     * @return default value
     */
    String getDefaultValue();
}
