/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import java.util.HashMap;
import java.util.Map;

/**
 * Sistemde tanımlı olan DimentionLarın listesini tutar. Gerekli lookup fonksiyonlarını sağlar.
 * 
 * @author Hakan Uygun
 */
public class UnitSetRegistery {
    
    private static final Map<String, UnitSet> UNITSET_MAP = new HashMap<>();
    
    public static void register( UnitSet unitSet ){
        UNITSET_MAP.put(unitSet.getDimensionName(), unitSet);
    }
    
    
    public static UnitSet getUnitSet( String name ){
        return UNITSET_MAP.get(name);
    }
    
}
