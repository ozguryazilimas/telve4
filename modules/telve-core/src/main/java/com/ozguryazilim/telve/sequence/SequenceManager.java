package com.ozguryazilim.telve.sequence;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Seri numarası üretmek için API.
 * 
 * uygulamalarda davamlılık isteyen sıra-seri numarası üretim işleri için API
 * 
 * Değerleri farklı implementasyon storelar üzerinde tutabilir. 
 * 
 * Bu değerin produce edilmesi gerek.
 * 
 * - SequenceMemoryStore ( Test için )
 * - SequenceJpaStore ( Kullanım için )
 * - İleride perfomans için Key-Value veri tabanı olabilir.
 * 
 * Kahve üzerinden de bir Store yapıldı ama aslında bu işi doğrudan kahve üzerine almak lazım... Sayı işlemleri için AtomicInteger daha doğru.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class SequenceManager implements Serializable{
    
    @Inject
    private SequenceStore sequenceStore;

    /**
     * Geriye verilen serial için verilen uzunlukta sıfırlar doldurarak yeni numara döndürür.
     * 
     * Örnek: 000001
     * 
     * @param serial
     * @param len
     * @return 
     */
    public String getNewNumber(String serial, Integer len){
        Long l = sequenceStore.getNextValue(serial);
        String fmt="%0"+len+"d";
        return String.format(fmt, l);
        
    }
    
    /**
     * Geriye verilen seri numarasını ekleyerek yeni numara döndürür.
     * 
     * Örnek : AA-000001
     * 
     * @param serial
     * @param len
     * @return 
     */
    public String getNewSerialNumber(String serial, Integer len){
        return serial + "-" + getNewNumber(serial, len);
    }
}
