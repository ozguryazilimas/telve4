package com.ozguryazilim.telve.api.module;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.inject.Typed;

/**
 * Telve Module Tanım Bilgilerini tutar.
 * 
 * @author Hakan Uygun
 */
@Typed()
public class TelveModuleRegistery {
    
    private static final List<String> moduleNames = new ArrayList<String>();
    private static final List<String> permissonFileNames = new ArrayList<String>();
    private static final List<String> messageBundleNames = new ArrayList<String>();
    
    /**
     * İsmi verilen modülü register eder.
     * @param name 
     */
    public static void register( String name, String permFile, String messageBundle ){
        moduleNames.add(name);
        permissonFileNames.add(permFile);
        messageBundleNames.add(messageBundle);
    }
    
    
    /**
     * Geriye sisteme tanımlı olan module listeni döndürür.
     * @return 
     */
    public static List<String> getModuleNames() {
        return moduleNames;
    }

    /**
     * Adı verilen modulün tanımlı olup olmadığını döndürür.
     * @param moduleName
     * @return 
     */
    public static boolean isModuleRegistered( String moduleName ){
        return moduleNames.contains(moduleName);
    }

    public static List<String> getPermissonFileNames() {
        return permissonFileNames;
    }

    public static List<String> getMessageBundleNames() {
        return messageBundleNames;
    }

}

