package com.ozguryazilim.telve.help;

/**
 * Uygulamalar bu provider interface'ini implemente ederek online help için gerekli URL'i üretebilirler.
 * 
 * Genelde viewRoot'a bakarak hangi sayfa için online help istendiğine bakıp buna göre değer değişiklikleri yapılabilir.
 * 
 * @author oyas
 */
public interface HelpResolver {
    /**
     * Bu provider'ın işlemi kabul edip etmeyeceği
     * 
     * @return 
     */
    boolean canHandle();
    
    /**
     * Geriye help yolu için contextPath üzerinden yol dönmeli.
     * 
     * Örnek: /infocenter/docs?topic=deneme
     * 
     * @return 
     */
    String getHelpPath();
    
    /**
     * Öncelik sıra değeri.
     * 
     * Önce değerlendirmeye girmesi istenen resolver'ın order değeri küçük olmalıdır.
     * 
     * Default HelpResolver order değeri 1000
     * @return 
     */
    Integer getOrder();
}
