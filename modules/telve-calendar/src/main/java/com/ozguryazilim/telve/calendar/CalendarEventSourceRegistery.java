/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.ozguryazilim.telve.calendar.annotations.CalendarEventSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hakan Uygun
 */
public class CalendarEventSourceRegistery {
    
    private static final Logger LOG = LoggerFactory.getLogger(CalendarEventSourceRegistery.class);
    
    private static final Map<String,CalendarEventSource> registery = new HashMap<>();
    
    /**
     * Sisteme CalendarEventSource register eder.
     * @param name
     * @param a 
     */
    public static void register( String name, CalendarEventSource a ){
        registery.put(name, a);
        LOG.info("Registered Calendar Event Source  : {}", name );
    }
    
    /**
     * Geriye ismi verilen kaynak için metaData bilgisini döndürür.
     * @param name
     * @return 
     */
    public static CalendarEventSource getMetadata( String name ){
        return registery.get(name);
    }
    
    /**
     * Geriye register edilmiş event source listesini döndürür.
     * @return 
     */
    public static List<String> getRegisteredEventSources(){
        return new ArrayList<>(registery.keySet());
    }
}
