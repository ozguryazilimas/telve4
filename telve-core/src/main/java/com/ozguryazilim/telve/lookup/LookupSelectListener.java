/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

/**
 * Lookup üzerinden seçim sonuçlarını dinlemek için callback arayüzü.
 * 
 * Özellikle EntityFilter'lar kullanır.
 * 
 * @author Hakan Uygun
 */
public interface LookupSelectListener {
    
    /**
     * Bir seçim gerçekleştiğinde bu mthod çağrılır.
     * @param o 
     */
    public void onSelect( Object o );
    
}
