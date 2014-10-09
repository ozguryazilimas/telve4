/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import com.ozguryazilim.telve.entities.Option;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.picketlink.Identity;

/**
 * Telve ayar verilerini saklar.
 * 
 * FIXME: Burada güncelleme performansı için bişiler yapmak lazım. COnfigResolver ile tam çalıştığına emin değilim...
 * 
 * @author Hakan Uygun
 */
@Singleton
@ApplicationScoped
public class TelveConfigRepository {
    
    /**
     * Veri tabanında bulunan değerleri chacler.
     */
    private Map<String,String> properties;
    
    @Inject
    private EntityManager entityManager;
    
    @Inject 
    private Cache<String, Option> cache;
    
    @Inject @Any
    private Identity identity;
    
    @PostConstruct
    public void init(){
        properties = new HashMap<>();
        List<Option> ls = entityManager.createQuery("select c from Option c").getResultList();
        for( Option o : ls ){
            properties.put(o.getKey(), o.getValue());
            cache.put(o.getKey(), o);
        }
    }
    
    /**
     * Verilen anahtar'la property döner.
     * Bulamazsa null döner. getOption().getAsString() çağrısı.
     * @param key
     * @return 
     */
    public String getProperty( String key ){
        Option o = getOption(key);
        return o == null ? null : o.getAsString();
    }
    
    
    public String getProperty( OptionKey key ){
        return getProperty(key.getValue());
    }
    
    /**
     * Boş map döner. 
     * Kullanılmamalı.
     * @return 
     */
    public Map<String,String> getProperties(){
        return Collections.EMPTY_MAP;
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
    
    public void setProperty( OptionKey key, String value){
        setProperty(key.getValue(), value);
    }
    
    /**
     * Verilen anahtar için option döner.
     * Eğer yoksa verilen defaultValue ile döner.
     * @param key
     * @param defaultValue
     * @return 
     */
    public Option getOption( String key, String defaultValue ){
        Option o = getOption(key);
        if( o == null ){
            o = new Option();
            o.setKey(key);
            o.setValue(defaultValue);
        }
        
        return o;
    }
    
    public Option getOption( OptionKey key ){
        return getOption( key.getValue(), key.getDefaultValue());
    }
    
    /**
     * Verilen Anahtar için option döner.
     * @param key
     * @return 
     */
    public Option getOption( String key ){
        Option o = cache.get(key);
        if( o == null ){
            //Veri tabanına bak
            o = getOptionFromEntity( key );
            if( o != null ){
                cache.put(key, o);
            }
        }
        return o;
    }
    
    /**
     * İstenilen option'ı veri tabanından çeker.
     * Bulamazsa null döner.
     * 
     * @param key
     * @return 
     */
    protected Option getOptionFromEntity( String key ){
        List<Option> ls = entityManager.createQuery("select c from Option c where key = :key ")
                .setParameter( "key", key)
                .getResultList();
        
        return  ls.isEmpty() ? null : ls.get(0);
    }
    
    /**
     * Verilen değeri veri tabanına ve cache'e ekler.
     * @param o 
     */
    @Transactional
    public void saveOption( Option o ){
        
        Option o2 = getOption( o.getKey() );
        
        if( o2 != null ){
            //Veri tabanında daha önce varmış...
            if( !o2.getValue().equals( o.getValue() ) ){
                //Değer zaten aynı ise niye saklayalım ki?
                o2.setValue(o.getValue());
                entityManager.merge(o2);
            }
        } else {
            //Veri tabanında daha önce yokmuş
            entityManager.persist(o);
            cache.put(o.getKey(), o);
        }
    }
    
    public void updateProperties( List<Option> ls ){
        for( Option o : ls ){
            saveOption(o);
        }
    } 
    
    
    /**
     * Verilen key'e göre cache'i ısıtır :)
     * @param key 
     */
    public void warmup( String key ){
        List<Option> ls = entityManager.createQuery("select c from Option c where key like :key ")
                .setParameter( "key", key +"%")
                .getResultList();
        
        for( Option o : ls ){
            cache.put(o.getKey(), o);
        }
    }
    
    /**
     * Verilen key'e göre cache'i ısıtır :)
     * @param key 
     */
    public void warmupUserAware( String key ){
        
        key = identity.getAccount().getId() + "." + key;
        
        List<Option> ls = entityManager.createQuery("select c from Option c where key like :key ")
                .setParameter( "key", key +"%")
                .getResultList();
        
        for( Option o : ls ){
            cache.put(o.getKey(), o);
        }
    }
    
    /**
     * Verilen anahtarın aktif kullanıcıya göre Option döndürür.
     * 
     * Eğer kullanıcı için yoksa "SYSTEM" için var mı diye bakar.
     * 
     * TODO: Benzer şekilde ROLE, GRUP v.b. kontrollerde eklemek lazım...
     * 
     * @param key
     * @return 
     */
    public Option getUserAwareOption( String key ){
        Option o = getOption( identity.getAccount().getId() + "." + key);
        if( o == null){
            o = getOption( "SYSTEM." + key);
        }
        
        return o;
    }
    
    public Option getUserAwareOption( OptionKey key ){
        return getUserAwareOption( key.getValue());
    }
    
    /**
     * Verilen anahtarın aktif kullanıcıya göre Option döndürür.
     * 
     * Eğer kullanıcı için yoksa "SYSTEM" için var mı diye bakar.
     * 
     * TODO: Benzer şekilde ROLE, GRUP v.b. kontrollerde eklemek lazım...
     * 
     * eğer bulamazsa içine key'i konmuş ama value boş bir option döndürür.
     * 
     * @param key
     * @return 
     */
    public Option getUserAwareOption( String key, boolean create ){
        Option o = getUserAwareOption(key);
        if( o == null){
            o = new Option();
            o.setKey( identity.getAccount().getId() + "." + key);
        }
        
        return o;
    }
    
    public Option getUserAwareOption( OptionKey key, boolean create ){
        return getUserAwareOption( key.getValue(), create);
    }
    
    /**
     * Verilen Option'ı kullanıcı bilgisine göre saklar.
     * 
     * TODO: Role, Grup  kontrolleri de yapılmalı.
     * 
     * @param o 
     */
    public void saveUserAvareOption( Option o ){
        if( o.getKey().startsWith("SYSTEM") ){
            String k = o.getKey();
            k = k.replaceFirst("SYSTEM", identity.getAccount().getId());
            o.setKey(k);
        } else if( !o.getKey().startsWith(identity.getAccount().getId()) ){
            o.setKey( identity.getAccount().getId() + "." + o.getKey());
        }
        saveOption(o);
    }
    
    
    public static TelveConfigRepository instance(){
        //Daha BeanManager init edilmeden önce çağrılırsa geriye null dönecek
        try{
            return BeanProvider.getContextualReference(TelveConfigRepository.class, true);
        } catch ( IllegalStateException e ){
            return null;
        }
    }
    
}
