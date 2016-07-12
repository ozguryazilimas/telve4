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
public class DimensionRegistery {
    
    private static final Map<String, Dimension> DIMENSION_MAP = new HashMap<>();
    
    public static void register( Dimension dimension ){
        DIMENSION_MAP.put(dimension.getDimensionName(), dimension);
    }
    
    
    public static Dimension getDimension( String name ){
        return DIMENSION_MAP.get(name);
    }
    
}
