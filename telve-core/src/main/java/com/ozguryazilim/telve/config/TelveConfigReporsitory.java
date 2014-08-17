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
 * FIXME: Burada güncelleme performansı için bişiler yapmak lazım. COnfigResolver ile tam çalıştığına emin değilim...
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

    public void setProperty( String key, String value){
        properties.put(key, value);
        List<Option> ls = entityManager.createQuery("select c from Option c where key = :key ")
                .setParameter( "key", key)
                .getResultList();
        //Veri tabanında yok demek
        if( ls.isEmpty() ){
            Option o = new Option();
            o.setKey(key);
            o.setValue(value);
            entityManager.persist(o);
        } else {
            Option o = ls.get(0);
            o.setValue(value);
            entityManager.merge(o);
        }
        entityManager.flush();
    }
    
    
    public void updateProperties( Map<String, String> props ){
        //TODO: Burayı en optimize nasıl yazarız?
    } 
    
    public static TelveConfigReporsitory instance(){
        //Daha BeanManager init edilmeden önce çağrılırsa geriye null dönecek
        try{
            return BeanProvider.getContextualReference(TelveConfigReporsitory.class, true);
        } catch ( IllegalStateException e ){
            return null;
        }
    }
    
}
