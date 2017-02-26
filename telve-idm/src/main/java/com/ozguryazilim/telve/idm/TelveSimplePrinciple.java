/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
}
