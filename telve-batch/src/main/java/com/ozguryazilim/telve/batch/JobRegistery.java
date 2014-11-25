/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import java.util.ArrayList;
import java.util.List;

/**
 * Sistemde tanımlı olan JBatch Job'larının listesini tuttar.
 * 
 * TODO: Autodiscovery yapacak bir yol geliştirelim : Classpath tarayarak /META-INF/batch-jobs/*.xml toplasın
 *  
 * 
 * @author Hakan Uygun
 */
public class JobRegistery {
    
    
    private static final List<String> jobNames = new ArrayList<>();
    
    /**
     * İsmi verilen job'u sisteme kaydeder.
     * 
     * TODO: Verilen isimli Job olup olmadığını kontrol etsek mi?
     * 
     * @param name 
     */
    public static void register(String name){
        if( !jobNames.contains(name) ){
            jobNames.add(name);
        }
    }
    
    
    public static List<String> getJobNames(){
        return jobNames;
    }
    
}
