/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.image;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.FeaturePointer;
import java.io.InputStream;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FeatureImage için Service API.
 * 
 * TODO: imaj idlerine göre Backend bulma işlemi yapılacak.
 * 
 * @author Hakan Uygun
 */
@Named
@ApplicationScoped
public class ImageService implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    
    public static final String DEFAULT_ASPECT = "ORIGINAL";
    public static final String DEFAULT_PLACEHOLDER = "DEFAULT";
    public static final String PLACEHOLDER_ID_PREFIX = "PH";
    
    @Inject
    private Identity identity;
    
    @Inject
    private ImageStore imageStore;
    
    @Inject
    private PlaceholderImageService placeholderImageService;
    
    
    /**
     * Verilen BusinessKey, Aspect ve Placeholder bilgilerine bakarak imageId'sini bulmaya çalışır.
     * 
     * Geriye mutlaka bir ImageId'si döner. Hiç bir şey bulamaz ise Placeholder bilgisi dönecektir.
     * 
     * @param bk
     * @param aspect
     * @param placeholder
     * @return 
     */
    public String getImageId( String bk, String aspect, String placeholder ){
        
        String fpp = imageStore.getImageId( bk, aspect );
        
        //Eğer id gelmedi ise ve aspect de default değil ise o zaman default aspect için id isteyeleim.
        if( Strings.isNullOrEmpty(fpp) && !DEFAULT_ASPECT.equals(aspect)){
            fpp = imageStore.getImageId( bk, DEFAULT_ASPECT );
        }
        
        //Eğer hala bir id yoksa bu sefer placeholder idsi isteyelim
        if( Strings.isNullOrEmpty(fpp) ){
            return PLACEHOLDER_ID_PREFIX + "-" + placeholderImageService.getImageId( bk, placeholder );
        }

        return fpp;
    }
    
    public String getImageId( String bk, String placeholder ){
        return getImageId(bk, DEFAULT_ASPECT, placeholder);
    }
    
    public String getImageId( String bk ){
        return getImageId(bk, DEFAULT_ASPECT, DEFAULT_PLACEHOLDER);
    }
    
    public String getImageId( FeaturePointer fp, String aspect, String placeholder ){
        return getImageId(getBusinessKey(fp), aspect, placeholder);
    }
    
    public String getImageId( FeaturePointer fp, String placeholder ){
        return getImageId(getBusinessKey(fp), DEFAULT_ASPECT, placeholder);
    }
    
    public String getImageId( FeaturePointer fp ){
        return getImageId(getBusinessKey(fp), DEFAULT_ASPECT, DEFAULT_PLACEHOLDER);
    }
    
    
    /**
     * Eğer identity varsa onun bilgisini yoksa SYSTEM döner.
     * @return 
     */
    protected String findOwner(){
        return identity != null  ? identity.getLoginName() : "SYSTEM";
    }
    
    /**
     * FeaturePointer'dan image için kullanılacak BusinessKey'i döndürür.
     * @param fp
     * @return 
     */
    protected String getBusinessKey( FeaturePointer fp ){
        return fp.getFeature() + "-" + fp.getPrimaryKey();
    }
    
    public void saveImage( String businessKey, String aspect, InputStream image, String mimeType, String info ){
        imageStore.save(businessKey, aspect, image, mimeType, info, findOwner());
    }
    
    public void saveImage( String businessKey, InputStream image, String mimeType, String info ){
        imageStore.save(businessKey, DEFAULT_ASPECT, image, mimeType, info, findOwner());
    }
    
    public void saveImage( FeaturePointer fp, String aspect, InputStream image, String mimeType, String info ){
        imageStore.save(getBusinessKey( fp), aspect, image, mimeType, info, findOwner());
    }
    
    public void saveImage( FeaturePointer fp, InputStream image, String mimeType, String info ){
        imageStore.save(getBusinessKey( fp), DEFAULT_ASPECT, image, mimeType, info, findOwner());
    }
    
    
    public void deleteImage( String businessKey ){
        imageStore.delete(businessKey);
    }
    
    public void deleteImage( String businessKey, String aspect ){
        imageStore.delete(businessKey, aspect);
    }
    
    public void deleteImage( FeaturePointer fp ){
        imageStore.delete(getBusinessKey(fp));
    }
    
    public void deleteImage( FeaturePointer fp, String aspect ){
        imageStore.delete(getBusinessKey(fp), aspect);
    }
    
    
    public InputStream getImage( String imageId ){

        LOG.debug("Requested Image : {}", imageId);
        
        if( imageId.startsWith(PLACEHOLDER_ID_PREFIX)){
            //Prefixten sonrasını gönderiyoruz. Prefix kısmı bizim için çünkü
            String ss = imageId.substring(PLACEHOLDER_ID_PREFIX.length());
            return placeholderImageService.getImage(ss);
        }

        InputStream is = imageStore.getImageAsStream(imageId);
        if( is == null ){
            LOG.warn("Requested Image not found: {}", imageId);
            return placeholderImageService.getImage("placeholder");
        }
        
        return is;
    }
    
}
