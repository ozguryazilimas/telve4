package com.ozguryazilim.telve.attachment;

import com.ozguryazilim.telve.entities.FeaturePointer;

/**
 * AttachmentContext üretimi için Factory API.
 * 
 * Özellikle arayüz tarafında nasıl bir Context kullanılacağını belirleme için yardımcı sınıf
 * 
 * Seçim sırasında önce Feature+Payload sonra sadece paylod en son feature ile sorar.
 * 
 * @author Hakan Uygun
 */
public interface AttachmentContextProvider {

    /**
     * Feature + Payload'a göre filtre
     * 
     * @param featurePointer
     * @param payload
     * @return 
     */
    boolean canHandle( FeaturePointer featurePointer, Object payload );
    
    /**
     * Eğer feature + payload'a göre filtre sonucu bulunamadı ise sadece payload'a bakan bir tane
     * 
     * @param payload
     * @return 
     */
    boolean canHandle( Object payload );
    
    /**
     * Payload'la ilgilenmeyip sadece feature ile ilgilenenleri sorar.
     * 
     * @param featurePointer
     * @return 
     */
    boolean canHandle( FeaturePointer featurePointer );
    
    /**
     * Öncelik sıralaması
     * 
     * düşük öncelik öne alınır.
     * 
     * Telve Defaultları 500-400 arasıdır.
     * 
     * @return 
     */
    int priority();
    
    /**
     * Geriye attachmentlar için kullanılacak olan Context döndürülür.
     * 
     * Context'i belirlemek için verilen payload'ı kullanabilir.
     * 
     * @param payload
     * @return 
     */
    AttachmentContext getContext( FeaturePointer featurePointer, Object payload );
    
}
