/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemde tanımlı feature'ların listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class FeatureRegistery {
   
    public static final Logger LOG = LoggerFactory.getLogger(FeatureRegistery.class);
    
    private static Map<String,Feature> featureNameMap = new HashMap<>();
    private static Map<Class,Feature> featureClassMap = new HashMap<>();
    private static Map<String,Class> beanNameMap = new HashMap<>();
    private static Map<Class,Class> beanClassMap = new HashMap<>();
    
    public static void register( Feature a, Class annoatedClass ){
        
        String featureName = a.name();
        if( Strings.isNullOrEmpty( featureName )){
            //FIXME: Burada Sınıf tipine bakılacak eğer Object ise annotate edilen sınıf ismi kullanılacak
            if( a.forEntity().equals(Object.class) ){
                featureName = annoatedClass.getSimpleName();
            } else {
                featureName = a.forEntity().getSimpleName();
            }
        } 
        
        featureNameMap.put(featureName, a);
        beanNameMap.put(featureName, annoatedClass);
        
        if( !a.forEntity().equals(Object.class) ) {
            featureClassMap.put(a.forEntity(), a);
            beanClassMap.put(a.forEntity(), annoatedClass);
        }
        
        LOG.info("Featue {} is registered for class {}", featureName, a.forEntity().getClass().getSimpleName());
    }
    
    
    public static FeatureHandler getHandler( String name ){
        //TODO: NPE check
        return (FeatureHandler) BeanProvider.getContextualReference( beanNameMap.get(name), true);
    }
    
    public static FeatureHandler getHandler( Class clazz ){
        //TODO: NPE check
        return (FeatureHandler) BeanProvider.getContextualReference( beanClassMap.get(clazz), true);
    }
    
    //TODO: caption, icon, permission gibi şeyler için getter yazmak lazım
    public static String getCaption( String name ){
        Optional<Feature> fa = Optional.ofNullable(featureNameMap.get(name));
        return fa.isPresent() ? fa.get().caption() : null;
    }
    
    public static String getIcon( String name ){
        Optional<Feature> fa = Optional.ofNullable(featureNameMap.get(name));
        return fa.isPresent() ? fa.get().icon() : null;
    }
    
    public static String getCaption( Class clazz ){
        Optional<Feature> fa = Optional.ofNullable(featureClassMap.get(clazz));
        return fa.isPresent() ? fa.get().caption() : null;
    }
    
    public static String getIcon( Class clazz ){
        Optional<Feature> fa = Optional.ofNullable(featureClassMap.get(clazz));
        return fa.isPresent() ? fa.get().icon() : null;
    }
    
}
