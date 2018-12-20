package com.ozguryazilim.telve.bpm.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Human Task Handler Registery.
 * @author Hakan Uygun
 */
public class HumanTaskHandlerRegistery {

    
    private static final Logger LOG = LoggerFactory.getLogger(ProcessHandlerRegistery.class);
    
    /**
     * Human taskHandler sınıf isimleri
     */
    private static final Map< String, HumanTaskHandler> humanTasksHandlers = new HashMap<>();
    /**
     * Human Task key isimleri ile handler eşleme
     */
    private static final Map< String, String> humanTaskNames = new HashMap<>();
    
    /**
     * Sisteme yeni bir HumanTaskHandler meta datası ekler.
     * @param name humansTaskHandler sınıfının EL adı
     * @param a 
     */
    public static void register( String name, HumanTaskHandler a) {
        humanTasksHandlers.put( name, a);
        humanTaskNames.put(a.taskName(), name);
        LOG.info("HumanTaskHandler registered {}", name);
    }
    
    /**
     * İsmi verilen task için icon adı döndürür.
     * @param taskName
     * @return 
     */
    public static String getIconName( String taskName ){
        String s = humanTaskNames.get(taskName);
        if( s == null ){
            return "fa-gears";
        }
        
        return humanTasksHandlers.get(s).icon();
    }
    
    /**
     * İsmi verilen task için handler döndürür.
     * @param taskName
     * @return 
     */
    public static String getHandlerName( String taskName ){
        return humanTaskNames.get(taskName);
    }
    
    /**
     * Geriye tanımlı olan human task isimlerinin listesini döndürür.
     * @return 
     */
    public static List<String> getTaskNames(){
        return new ArrayList<String>(humanTaskNames.keySet());
    }
    
}
