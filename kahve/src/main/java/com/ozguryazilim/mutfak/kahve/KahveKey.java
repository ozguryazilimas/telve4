/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
