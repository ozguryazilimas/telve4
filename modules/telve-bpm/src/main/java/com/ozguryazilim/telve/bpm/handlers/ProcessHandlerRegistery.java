package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemde tanımlı process handler kayıtlarını tuttar.
 * @author Hakan Uygun
 */
public class ProcessHandlerRegistery {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProcessHandlerRegistery.class);
    
    //Sistemde kayıtlı olan ProcessHandler tanımları
    private static final Map< String, ProcessHandler> processes = new HashMap<>();
    
    
    /**
     * Sisteme yeni bir ProcessHandler meta datası ekler.
     * @param name processHandler sınıfının EL adı
     * @param a 
     */
    public static void register( String name, ProcessHandler a) {
        processes.put( name, a);
        LOG.info("ProcessHandler registered {}", name);
    }
    
    /**
     * Geriye sisteme kayıtlı olan ProcessHanler işaretçilerini döndürür.
     * @return 
     */
    public static Collection<ProcessHandler> getHandlers(){
        return processes.values();
    }

    /**
     * Geriye process handler isimlerini döndürür.
     * @return 
     */
    public static List<String> getProcessHandlerNames(){
        return new ArrayList(processes.keySet());
    }

    /**
     * Geriye Start popup desteği olan process isimlerini döndürür.
     * 
     * @return 
     */
    public static List<String> getStartableProcessHandlerNames(){
        List<String> ls = new ArrayList();
        for( Map.Entry<String,ProcessHandler> e : processes.entrySet()){
            if( e.getValue().hasStartDialog() ){
                ls.add(e.getKey());
            }
        }
        return ls;
    }
    
    /**
     * Geriye sistemde tanımlı process isimlerini döndürür.
     * 
     * Burada temel alınan BPM'e yüklenmiş olanlar değil uygulama tanımlı handler'lar olacak.
     * 
     * @return 
     */
    public static List<String> getProcessNames(){
        List<String> ls = new ArrayList();
        for( Map.Entry<String,ProcessHandler> e : processes.entrySet()){
            //TODO: Burada aslında bir yetki kontrolü lazım sanırım.
            ls.add(e.getValue().processId());
        }
        return ls;
    }
    
    /**
     * Verilen processHandler ismi için tanımlı ikon pathi döndürür.
     * 
     * Eğer özel olarak tanımlı yoksa varsayılan ikon ismi döndürülür.
     * 
     * @param name
     * @return 
     */
    public static String getIconPath( String name ){
        String ip = processes.get(name).icon();
        
        return Strings.isNullOrEmpty(ip) ? "/img/ribbon/small/taskConsole.png" : ip;
    }
    
}
