package com.ozguryazilim.telve.image.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.FeaturePointer;
import com.ozguryazilim.telve.image.ImageService;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FeatureImage için Widget Controller
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class FeatureImageWidget implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(FeatureImageWidget.class);
    
    @Inject
    private Identity identity;
    
    @Inject
    private ImageService featureImageService;
    
    @Inject
    private SimpleImageEditor editor;
    
    /**
     * Widget'in hangi Feature için çalıştığının göstergesi
     */
    private FeaturePointer featurePointer;
    private String placeholder;
    private String imageId;
    
    public void init( FeaturePointer featurePointer, String placeholder ){
        this.featurePointer = featurePointer;
        this.placeholder = placeholder;
        this.imageId = null;
    }
    
    public String getImageId(){
        if( Strings.isNullOrEmpty(imageId) ){
            imageId = featureImageService.getImageId(featurePointer, "THUMBNAIL", placeholder);
        }
        return imageId;
    }
    
    public void edit(){
        imageId = null;
        editor.openDialog(featurePointer, placeholder);
    }
    
}
