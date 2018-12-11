/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.sequence;

/**
 * Sequence saklama API'i.
 * 
 * @author Hakan Uygun
 */
public interface SequenceStore {
    
    /**
     * Verilen key'e ait son değeri döndürür.
     * 
     * eğer verilen key için bir değer yoksa geriye 0l döndürür.
     * 
     * @param key
     * @return 
     */
    Long findLastValue( String key );
    
    /**
     * Verilen key için bir sonraki değeri döndürür fakat değeri değiştirmez.
     * 
     * @param key
     * @return 
     */
    Long findNextValue( String key );
    
    /**
     * Verilen key için bir sonraki değeri döndürür ve değeri değiştirir.
     * 
     * @param key
     * @return 
     */
    Long getNextValue( String key );
    
    /**
     * Verilen key için verilen değeri saklar.
     * @param key
     * @param value 
     */
    void saveValue( String key, Long value );
    
}
