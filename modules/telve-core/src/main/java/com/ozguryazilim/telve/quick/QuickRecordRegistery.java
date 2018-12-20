package com.ozguryazilim.telve.quick;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemdeki QuickRecord bileşenşlerinin listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class QuickRecordRegistery {
   
    private static final Logger LOG = LoggerFactory.getLogger(QuickRecordRegistery.class);
    
    //BeanName ve MetaData
    private static final Map< String, QuickRecord > quickRecordMap = new HashMap<>();
    
    /**
     * Sisteme yeni bir quickReport meta datası ekler.
     * @param name rapor sınıfının EL adı
     * @param a 
     */
    public static void register( String name, QuickRecord a) {
        quickRecordMap.put( name, a);
        LOG.info("QuickRecord registered {}", name);
    }
    
    public static Map< String, QuickRecord > getQuickRecordMap(){
        return quickRecordMap;
    }
}
