package com.ozguryazilim.telve.reports;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemde tanımlı olan raporın bilgilerini tutar.
 *
 * @author Hakan Uygun
 */
public class ReportRegistery {

    private static final Logger LOG = LoggerFactory.getLogger(ReportRegistery.class);
    
    private static final Map< String, Report> reports = new HashMap<>();

    /**
     * Sisteme yen bir raport meta datası ekler.
     * @param name rapor sınıfının EL adı
     * @param a 
     */
    public static void register( String name, Report a) {
        reports.put( name, a);
        LOG.info("Report registered {}", name);
    }

    /**
     * Rapor Meta data listesini döndürür.
     * @return 
     */
    public static Map< String, Report> getReports() {
        return reports;
    }
    
}
