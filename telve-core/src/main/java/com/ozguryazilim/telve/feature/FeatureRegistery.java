/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.feature.search.AbstractFeatureSearchHandler;
import com.ozguryazilim.telve.feature.search.Search;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sistemde tanımlı feature'ların listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class FeatureRegistery {
   
    public static final Logger LOG = LoggerFactory.getLogger(FeatureRegistery.class);
    
    /**
     * İsim üzerinden Feature Annotation'ı map'i İsim aslında sınıf ismi 
     * Örnek : ContactFeature
     */
    private static Map<String,Feature> featureNameMap = new HashMap<>();
    /**
     * Entity sınıfı üzerinden Feature annotation'ı
     */ 
    private static Map<Class,Feature> featureClassMap = new HashMap<>();
    /**
     * Sınıf ismi üzerinden featureHandler sınıf tanımının kendisi
     */
    private static Map<String,Class> beanNameMap = new HashMap<>();
    /**
     * Entity sınıfı üzerinde featureHandler sınıfı
     */
    private static Map<Class,Class> beanClassMap = new HashMap<>();
    
    /**
     * Feature İsmi ile SearchHandler mappingi
     */
    private static Map<String,Class<? extends AbstractFeatureSearchHandler>> featureSearchMap = new HashMap<>();

    /**
     * Feature kategorisi ile Feature ismi mappingi
     */
    private static Map<FeatureCategory, ArrayList<String>> featureCategorySearchMap = new HashMap<>();
    
    public static void register( Feature a, Class annoatedClass ){
        //FIXME: name diye bir kolonumuz olmasın. Gereksiz kafa karıştırıcı olacak. Doğrudan sınıf adını kullanalım
        //forEntity için de ayrı bir sorgu hazırlayalım. 
        String featureName = annoatedClass.getSimpleName();
         
        
        featureNameMap.put(featureName, a);
        beanNameMap.put(featureName, annoatedClass);
        
        if( !a.forEntity().equals(Object.class) ) {
            featureClassMap.put(a.forEntity(), a);
            beanClassMap.put(a.forEntity(), annoatedClass);
        }
        
        Search sa = (Search) annoatedClass.getAnnotation(Search.class);
        FeatureCategory[] featureCategory = a.category();
        if( sa != null ){
            featureSearchMap.put(featureName, sa.handler());

            for (FeatureCategory fc : featureCategory) {
                mapFeatureNameToCategory(fc, featureName);
            }
        }
        
        
        LOG.info("Feature {} is registered for class {}", featureName, a.forEntity().getClass().getSimpleName());
    }
    
    
    /**
     * Geriye Sınıf ismi üzerinden FeatureHandler instance'ını döndürür.
     * Not: Default Qualifier'ı olan sınıfları arar. Dolayısı ile Voucher vb Quailifier ile işaretlenmiş sınıfların Default olarak da işaretlenmişolması gerekir.
     * @param name
     * @return 
     */
    public static FeatureHandler getHandler( String name ){
        //TODO: NPE check
        return (FeatureHandler) BeanProvider.getContextualReference( beanNameMap.get(name), true);
    }
    
    /**
     * Geriye Entity Sınıfı üzerinden FeatureHandler instance'ını döndürür.
     * Not: Default Qualifier'ı olan sınıfları arar. Dolayısı ile Voucher vb Quailifier ile işaretlenmiş sınıfların Default olarak da işaretlenmişolması gerekir.
     * @param name
     * @return 
     */
    public static FeatureHandler getHandler( Class clazz ){
        //TODO: NPE check
        return (FeatureHandler) BeanProvider.getContextualReference( beanClassMap.get(clazz), true);
    }
    
    /**
     * Geriye Entity Sınıfı üzerinden FeatureHandler sınıfını döndürür.
     * 
     * @param clazz Entity Class
     * @return 
     */
    public static Class<? extends FeatureHandler> getFeatureClass( Class clazz ){
        //TODO: NPE check
        return beanClassMap.get(clazz);
    }
    
    
    public static Class<? extends FeatureHandler> getFeatureClass( String featureName ){
        //TODO: NPE check
        return beanNameMap.get(featureName);
    }
    
    /**
     * Verilen İsim üzerinden caption key bilgisini döndürür.
     * 
     * @param name
     * @return 
     */
    public static String getCaption( String name ){
        Optional<Feature> fa = Optional.ofNullable(featureNameMap.get(name));
        if( fa.isPresent() && !Strings.isNullOrEmpty(fa.get().caption())){
            return fa.get().caption();
        } else {
            //Gelen isim zaten sınıf ismi conventionımız böyle
            return "feature.caption." + name;
        }
    }
    
        
    /**
     * Verilen Entity sınıfı üzerinden caption key bilgisini döndürür.
     * 
     * @param name
     * @return 
     */
    public static String getCaption( Class clazz ){
        Optional<Feature> fa = Optional.ofNullable(featureClassMap.get(clazz));
        if( fa.isPresent() && !Strings.isNullOrEmpty(fa.get().caption())){
            return fa.get().caption();
        } else {
            LOG.warn("Required feature fo Entity {} not found", clazz );
            return "";
        }
    }
    
    /**
     * Geriye sistemde tanımlı featureların isim listesini döndürür.
     * @return 
     */
    public static List<String> getFeatureNames(){
        return new ArrayList<>(featureNameMap.keySet());
    }
    
    /**
     * Geriye sistemde tanımlı searchable featureların isim listesini döndürür.
     * @return 
     */
    public static List<String> getSearchableFeatureNames(){
        return new ArrayList<>(featureSearchMap.keySet());
    }

    /**
     * Verilen kategoriye göre searchable featureların isim listesini döndürür.
     * @param category
     * @return
     */
    public static List<String> getSearchableFeatureNamesByCategory(FeatureCategory category) {
        ArrayList<String> featureList = featureCategorySearchMap.get(category);
        return featureList == null ? new ArrayList<>() : featureList;
    }

    public static AbstractFeatureSearchHandler getSearchHandler( String name ){
        //TODO: NPE check
        return (AbstractFeatureSearchHandler) BeanProvider.getContextualReference( featureSearchMap.get(name), true);
    }

    private static synchronized void mapFeatureNameToCategory(FeatureCategory fc,
                                                              String featureName) {
        ArrayList<String> featureNameList =  featureCategorySearchMap.get(fc);
        if(featureNameList == null) {
            featureNameList = new ArrayList<>();
            featureNameList.add(featureName);
        } else {
            featureNameList.add(featureName);
        }
        featureCategorySearchMap.put(fc, featureNameList);
    }
    
}
