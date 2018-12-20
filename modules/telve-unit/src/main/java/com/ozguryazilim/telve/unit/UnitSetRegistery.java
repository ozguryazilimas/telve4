package com.ozguryazilim.telve.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public static void clear(){
        UNITSET_MAP.clear();
    }
    
    public static UnitSet getUnitSet( String name ){
        return UNITSET_MAP.get(name);
    }
    
    public static List<String> getUnitSets(){
        return new ArrayList<>(UNITSET_MAP.keySet());
    }
    
}
