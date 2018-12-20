package com.ozguryazilim.telve.image;

import java.io.InputStream;
import java.io.Serializable;

/**
 * FeatureImage için Storage SPI.
 * 
 * @author Hakan Uygun
 */
public interface ImageStore extends Serializable{
   
    void save( String businessKey, String aspect, byte[] image, String mimeType, String info, String owner );
    void save( String businessKey, String aspect, InputStream image, String mimeType, String info, String owner );
    
    /**
     * Verilen key ile ilgili tüm imajları siler.
     * 
     * @param businessKey 
     */
    void delete( String businessKey );
    
    /**
     * Verilen key ile ilgili verilen aspect imajlarıı siler
     * @param businessKey
     * @param aspect 
     */
    void delete( String businessKey, String aspect );

    /**
     * Verilen bilgilere ait bir imaj var mı kontrolü yapar. 
     * 
     * Varsa true yoksa false döner.
     * 
     * @return 
     */
    boolean isImageExist( String businessKey );
    boolean isImageExist( String businessKey, String aspect );
    
    /**
     * Verilen business key için ilgili image'ın id'sini döndürür.Eğer birden fazla varsa ilkini döndürür.
     * 
     * eğer yoksa null döner.
     * 
     * BusinessKey aslında istenildiği gibi verilecek bir string olup bunun anlamını alttaki implmentasyon belirleyebilir.
     * Aynı durum aspect için de geçerli.
     * 
     * @param businessKey
     * @param aspect
     * @return 
     */
    String getImageId( String businessKey, String aspect );
    
    /**
     * Verilen id'ye sahip image'ı döndürür. Id formatı alttaki implementasyona özeldir.
     * 
     * @param imageId
     * @return 
     */
    byte[] getImage( String imageId );
    InputStream getImageAsStream( String imageId );
    
    
}
