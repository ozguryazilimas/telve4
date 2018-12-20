package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.TelveIdmPrinciple;
import java.io.Serializable;

/**
 *
 * @author oyas
 */
public class TelveSimplePrinciple implements TelveIdmPrinciple, Serializable{

    private final String name;

    public TelveSimplePrinciple(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    
}
