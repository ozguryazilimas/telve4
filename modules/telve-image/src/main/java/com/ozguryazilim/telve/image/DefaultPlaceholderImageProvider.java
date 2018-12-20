package com.ozguryazilim.telve.image;

import java.io.InputStream;

/**
 * Sistemde hiç başka provider olmasa bile bu provider imaj sağlar.
 * 
 * @author Hakan Uygun
 */
public class DefaultPlaceholderImageProvider implements PlaceholderImageProvider{

    @Override
    public String getImageId(String businessKey, String placeholder) {
        if( placeholder.contains("avatar")){
            return "avatar";
        }
        
        return "default";
    }

    @Override
    public InputStream getImage(String imageId) {
        if( "avatar".equals(imageId)){
            return this.getClass().getResourceAsStream("/defaultPersonImage.jpg");
        }
        return this.getClass().getResourceAsStream("/defaultCameraImage.jpg");
    }
    
}
