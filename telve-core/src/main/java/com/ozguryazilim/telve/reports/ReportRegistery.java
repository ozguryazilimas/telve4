/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import java.util.HashMap;
import java.util.Map;

/**
 * Sistemde tanımlı olan raporın bilgilerini tutar.
 *
 * @author Hakan Uygun
 */
public class ReportRegistery {

    private static final Map< String, Report> reports = new HashMap<>();

    /**
     * Sisteme yen bir raport meta datası ekler.
     * @param name rapor sınıfının EL adı
     * @param a 
     */
    public static void register( String name, Report a) {
        reports.put( name, a);
    }

    /**
     * Rapor Meta data listesini döndürür.
     * @return 
     */
    public static Map< String, Report> getReports() {
        return reports;
    }
    
}
