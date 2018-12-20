package com.ozguryazilim.telve.reports;

import java.util.HashMap;
import java.util.Map;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
public class SubreportRegistery {

    private static final Logger LOG = LoggerFactory.getLogger(SubreportRegistery.class);
    
    private static final Map< String, Subreport> reports = new HashMap<>();
    
    
    /**
     * Sisteme yen bir raport meta datası ekler.
     * @param name rapor sınıfının EL adı
     * @param a 
     */
    public static void register( String name, Subreport a) {
        reports.put( name, a);
        LOG.info("Subreport registered {}", name);
    }

    /**
     * Rapor Meta data listesini döndürür.
     * @return 
     */
    public static Map< String, Subreport> getReports() {
        return reports;
    }
    
    /**
     * Verilen Grup ve Feature için subreport implementasyonunu döndürür.
     * @param group
     * @param feature
     * @return 
     */
    public static AbstractSubreportBase getReport( String group, String feature) {
        for( Map.Entry<String, Subreport> ent : reports.entrySet()){
            if( group.equals( ent.getValue().group()) && feature.equals(ent.getValue().feature().getSimpleName())){
                return (AbstractSubreportBase) BeanProvider.getContextualReference(ent.getKey(),true);
            }
        }
        return null;
    }
}

