/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sisteme tanıtılmış komutların listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class CommandRegistery {
   
    private static final Logger LOG = LoggerFactory.getLogger(CommandRegistery.class);
    
    /**
     * Komut adı ve endpoint ikilisini tutar.
     */
    private static final Map< String, String> commands = new HashMap<>();
    
    /**
     * Sisteme yeni bir komut meta datası ekler.
     * @param name komut sınıfının EL adı
     * @param a 
     */
    public static void register( String name, String endpoint) {
        commands.put(name, endpoint);
        LOG.info("Registered Command : {}={}", name, endpoint);
    }
    
    /**
     * Geriye register edilen komutların camel endpoint listesini döndürür.
     * @return 
     */
    public static List<String> getEndpoints(){
        return new ArrayList(commands.values());
    }
    
    /**
     * Geriye tanımlanmış komut listesini döndürür.
     * @return 
     */
    public static List<String> getCommands(){
        return new ArrayList(commands.keySet());
    }
    
    /**
     * Verilen komutun sistemde tanımlı olup olmadığını kontrol eder.
     * @param command
     * @return 
     */
    public static boolean isRegistered( String command ){
        return commands.containsKey(command);
    }
    
    /**
     * Geriye verilen komutun endpoint bilgisini döndürür.
     * 
     * @param name
     * @return 
     */
    public static String getEndpoint( String name ){
        return commands.get(name);
    }
}
