/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Named;

/**
 * Sistemde tanımlı olan SubView'ların listesini tutar.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class SubViewRegistery implements Serializable{
   
    private Map<String,List<SubView>> parentMapper = new HashMap<>();
    
    <T> void processBean(@Observes ProcessBean<T> event) {
        System.out.println("SubView Registery Çalıştı");
        if (event.getAnnotated().isAnnotationPresent(SubView.class)) {
            SubView a = event.getAnnotated().getAnnotation(SubView.class);
            
            String parentName = a.master().getName();
            
            List<SubView> ls = parentMapper.get(parentName);
            if( ls == null ){
                ls = new ArrayList<SubView>();
                parentMapper.put(parentName, ls );
            }
            
            ls.add(a);
        }
    }

    /**
     * Geriye ismi verilen sınıfa ait SubView listesini döndürür.
     * @param parent
     * @return 
     */
    public List<SubView> getSubViews( String parent ){
        return parentMapper.get(parent);
    }
    
}
