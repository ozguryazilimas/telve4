/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.nav;

/**
 * Navigosyon sectionlarını tanımlamak için arayüz.
 * 
 * @author Hakan Uygun
 */
public interface NavigationSection extends Comparable<NavigationSection>{
   
    /**
     * Geriye Section label döndürür.
     * 
     * Bu değer dildosyası için key olabilir.
     * 
     * @return 
     */
    String getLabel();
    
    /**
     * Sıralama için değer döndürür.
     * 
     * @return 
     */
    Integer getOrder();
}
