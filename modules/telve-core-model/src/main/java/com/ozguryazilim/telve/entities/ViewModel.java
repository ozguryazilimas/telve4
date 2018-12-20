package com.ozguryazilim.telve.entities;

/**
 * Sunum katmanı için entity'den daha sadece model sınıfları için arayüz.
 * 
 * Browse, Lookup gibi tüm entity yerine sadece bir kısmını kullanmak için...
 * Eğer entity doğrudan kullanılacaksa onu da bu interface ile işaretlemek lazım...
 * 
 * @author Hakan Uygun
 */
public interface ViewModel {
    
    Long getId();
    
}
