package com.ozguryazilim.telve.sequence;

import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;

/**
 * Sequence değerlerini memory'de saklar.
 * 
 * Test amaçlıdır. Production'da kullanılmaması önerilir.
 * 
 * @author Hakan Uygun
 */
@RequestScoped @Alternative
public class SequenceMemoryStore implements SequenceStore{

    private ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
    
    @Override
    public Long findLastValue(String key) {
        Long l = map.get(key);
        return l == null ? 0 : l;
    }

    @Override
    public Long findNextValue(String key) {
        return findLastValue(key) + 1;
    }

    @Override
    public Long getNextValue(String key) {
        Long l = findNextValue(key);
        saveValue(key, l);
        return l;
    }

    @Override
    public void saveValue(String key, Long value) {
        map.put(key, value);
    }
    
}
