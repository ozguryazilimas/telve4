/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import com.ozguryazilim.telve.entities.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Telve ayar verilerini saklar.
 * 
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class TelveConfigReporsitory {
    
    /**
     * Veri tabanında bulunan değerleri chacler.
     */
    private Map<String,String> properties;
    
    @Inject
    private EntityManager entityManager;
    
    @PostConstruct
    public void init(){
        properties = new HashMap<>();
        List<Option> ls = entityManager.createQuery("select c from Option c").getResultList();
        for( Option o : ls ){
            properties.put(o.getKey(), o.getValue());
        }
    }
    
    public String getProperty( String key ){
        return properties.get(key);
    }
    
    public Map<String,String> getProperties(){
        return properties;
    }

    public static TelveConfigReporsitory instance(){
        //Daha BeanManager init edilmeden önce çağrılırsa geriye null dönecek
        System.out.println("TelveConfigReporsitory requested");
        try{
            
            return BeanProvider.getContextualReference(TelveConfigReporsitory.class, true);
        } catch ( IllegalStateException e ){
            System.out.println("TelveConfigReporsitory null");
            return null;
        }
    }
    
}
