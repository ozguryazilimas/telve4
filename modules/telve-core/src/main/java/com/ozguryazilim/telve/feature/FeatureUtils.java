package com.ozguryazilim.telve.feature;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.FeaturePointer;
import com.ozguryazilim.telve.utils.EntityUtils;

/**
 * Feature işlemleri için utility fonksiyonlar.
 * 
 * @author Hakan Uygun
 */
public class FeatureUtils {
   
    private FeatureUtils() {
    }

    /**
     * Verilen Feature sınıfını kullanarak featurePointer üretir.
     * 
     * @param featureClass
     * @param bizKey
     * @param pk
     * @return 
     */
    public static FeaturePointer getFeaturePointer(Class featureClass, String bizKey, Long pk) {
        FeaturePointer fp = new FeaturePointer();
        fp.setBusinessKey(bizKey);
        fp.setPrimaryKey(pk);
        fp.setFeature(featureClass.getSimpleName());

        return fp;
    }
    
    /**
     * Feature sınıf adını kullanarak featurePointer üretir.
     * @param featureClassName
     * @param bizKey
     * @param pk
     * @return 
     */
    public static FeaturePointer getFeaturePointer(String featureClassName, String bizKey, Long pk) {
        
        Class featureClass = FeatureRegistery.getFeatureClass(featureClassName);
        
        FeaturePointer fp = new FeaturePointer();
        fp.setBusinessKey(bizKey);
        fp.setPrimaryKey(pk);
        fp.setFeature(featureClass.getSimpleName());

        return fp;
    }
    
    /**
     * Verilen entity için verilen bilgilerle featurePointer üretir.
     * 
     * @param entity
     * @param bizKey
     * @param pk
     * @return 
     */
    public static FeaturePointer getFeaturePointer(EntityBase entity, String bizKey, Long pk) {
        
        if( entity == null ) return null;
        
        FeatureHandler handler = FeatureRegistery.getHandler(entity.getClass());
        
        if( handler== null) return null;
        
        FeaturePointer fp = new FeaturePointer();
        fp.setBusinessKey(bizKey);
        fp.setPrimaryKey(pk);
        fp.setFeature(handler.getName());

        return fp;
    }
    
    /**
     * Verilen entity için geriye feature pointer döndürür.
     * 
     * @param entity
     * @return 
     */
    public static FeaturePointer getFeaturePointer(EntityBase entity) {
       
        if( entity == null ) return null;
        
        FeatureHandler handler = FeatureRegistery.getHandler(entity.getClass());
        
        if( handler== null) return null;
        
        FeaturePointer fp = new FeaturePointer();
        fp.setBusinessKey(EntityUtils.getBizKeyValue(entity));
        fp.setPrimaryKey(entity.getId());
        fp.setFeature(handler.getName());

        return fp;
    }
}
