package com.ozguryazilim.telve.sequence;

import com.ozguryazilim.mutfak.kahve.Kahve;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Sequence durumlarını Kahve ile saklar.
 * 
 * FIXME: Aslında bu işi doğrudan kahveye doğru almak lazım.
 * 
 * @author Hakan Uygun
 */
@RequestScoped
public class SequenceKahveStore implements SequenceStore{

    private static final String SEQUENCE_KEY = "SEQUENCE.";
    
    @Inject
    private Kahve kahve;
    
    @Override
    public Long findLastValue(String key) {
        return kahve.get(SEQUENCE_KEY + key, 0l).getAsLong();
    }

    @Override
    public Long findNextValue(String key) {
        return findLastValue(key) + 1;
    }

    @Override
    public Long getNextValue(String key) {
        Long l = findNextValue(key);
        kahve.put(SEQUENCE_KEY + key, l);
        return l;
    }

    @Override
    public void saveValue(String key, Long value) {
        kahve.put(SEQUENCE_KEY + key, value);
    }
    
}
