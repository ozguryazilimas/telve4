package com.ozguryazilim.telve.image;

import java.io.InputStream;

/**
 * FeatureImage ve benzeri yerlerde henüz yüklenmemiş imajların yerine göstermek için kullanılabilecek imaj sağlayıcı API.
 * 
 * @author Hakan Uygun
 */
public interface PlaceholderImageProvider {
    
    /**
     * Bu sağlayıcı için unique ImageId.
     * 
     * Imaj istemek için bu ID ile gelinecek.
     * 
     * Hint parametresi farklı türde placeholderlar için verilebilir. Örneğin kişi için cinsiyet.
     * 
     * @return 
     */
    String getImageId( String businessKey, String placehoder );
    
    
    /**
     * ID'si verilen image geri döndürülür.
     * 
     * @param imageId
     * @return 
     */
    InputStream getImage( String imageId );
}
