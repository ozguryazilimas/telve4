/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Sistemde tanımlı olan SubView'ların listesini tutar.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class SubViewRegistery implements Serializable {

    private static final Map<String, List<SubView>> parentMapper = new HashMap<>();

    public static void register(SubView a) {
        String parentName = a.containerPage().getName();

        List<SubView> ls = parentMapper.get(parentName);
        if (ls == null) {
            ls = new ArrayList<>();
            parentMapper.put(parentName, ls);
        }

        ls.add(a);
    }

    /**
     * Kayıt edilmiş view'leri order ve isme göre sıralar.
     */
    public static void sort(){
        for( Map.Entry<String,List<SubView>> e : parentMapper.entrySet()){
            sort(e.getValue());
        }
    }
    
    
    /**
     * Verilen listeyi order ve name'e göre sıralar.
     * 
     * @param ls 
     */
    protected static void sort( List<SubView> ls ){
        
        Collections.sort(ls, new Comparator<SubView>() {

            @Override
            public int compare(SubView o1, SubView o2) {
                int result = o1.order() - o2.order() ;
                if( result == 0 ){
                    result = o1.viewPage().getName().compareTo(o1.viewPage().getName());
                }
                return result;
            }
        });
        
    }
    
    
    /**
     * Geriye ismi verilen sınıfa ait SubView listesini döndürür.
     *
     * @param parent
     * @return
     */
    public static List<SubView> getSubViews(String parent) {
        return parentMapper.get(parent);
    }

}
