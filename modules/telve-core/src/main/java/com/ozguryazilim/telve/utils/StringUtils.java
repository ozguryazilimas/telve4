/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.utils;

/**
 * Telve genelinde kullanılabilecek bazı string fonksiyonları.
 * 
 * @author Hakan Uygun
 */
public class StringUtils {
    
    
    /**
     * Verilen String içindeki Tükçe Karakterleri dengi ASCII'lere dönüştürür.
     * 
     * Örneğin : Ö O, ö o şeklinde.
     * 
     * TODO: Bu fonksiyon daha doğru bir şekilde yazılabilir.
     * 
     * @param val
     * @return 
     */
    public static String escapeTurkish( String val ){
        
        String result = val.replaceAll("Ö", "O");
        result = result.replaceAll("Ü", "U");
        result = result.replaceAll("Ğ", "G");
        result = result.replaceAll("İ", "I");
        result = result.replaceAll("Ş", "S");
        result = result.replaceAll("Ç", "C");
        result = result.replaceAll("ü", "u");
        result = result.replaceAll("ğ", "g");
        result = result.replaceAll("ı", "i");
        result = result.replaceAll("ş", "s");
        result = result.replaceAll("ç", "c");
        result = result.replaceAll("ö", "o");
        
        return result;
    }
    
}
