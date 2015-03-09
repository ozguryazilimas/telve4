/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dashboard;

import com.google.common.base.CaseFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Sistemde tanımlı olan dashlet bilgilerini tutar.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class DashletRegistery {

    private static final Map< String, Dashlet> dashlets = new HashMap<>();
    private static final Map< String, List<DashletCapability>> capabilities = new HashMap<>();

    /**
     * Sisteme yen bir dashlet meta datası ekler.
     * @param name rapor sınıfının EL adı
     * @param a 
     */
    public static void register( String name, Dashlet a) {
        dashlets.put( name, a);
        String s = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        capabilities.put(s, Arrays.asList( a.capability()));
    }

    /**
     * Dashlet Meta data listesini döndürür.
     * @return 
     */
    public static Map< String, Dashlet> getDashlets() {
        return dashlets;
    }

    /**
     * Bir dashlet'in 
     * @param dashlet
     * @param capability
     * @return 
     */
    public boolean hasCapability( String dashlet, DashletCapability capability ){
        List<DashletCapability> ls  = capabilities.get(dashlet);
        if( ls == null ) return false;
        
        return ls.contains(capability);
    }
    
}
