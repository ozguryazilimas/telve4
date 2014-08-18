/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Sistemde tanımlı olan OptionPane listesini tutar.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class OptionPaneRegistery {
   
    private static final Map< String, OptionPane> optionPanes = new HashMap<>();
    
    /**
     * Sisteme yeni bir OptionPane register eder.
     * @param name
     * @param a 
     */
    public static void register(String name, OptionPane a) {
        optionPanes.put(name, a);
    }

    /**
     * Sistemde tanımlı olan OptionPane listesini döndürür.
     * @return 
     */
    public static Map<String, OptionPane> getOptionPanes() {
        return optionPanes;
    }
    
    
}
