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
