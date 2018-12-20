package com.ozguryazilim.telve.image.jpa;

import com.ozguryazilim.telve.entities.SimpleImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ozguryazilim.telve.image.ImageStore;
import java.util.UUID;

/**
 *
 * @author oyas
*/
public class SimpleImageJpaStore implements ImageStore{

    private static final Logger LOG = LoggerFactory.getLogger(SimpleImageJpaStore.class);
    
    public SimpleImageRepository getRepository(){
        return BeanProvider.getContextualReference(SimpleImageRepository.class, true );
    }

    
    @Override
    public byte[] getImage(String imageId) {
        SimpleImage img = getRepository().findAnyByImageId(imageId);
        if( img == null ){
            return null;
        }
        return img.getContent();
    }

    @Override
    public InputStream getImageAsStream(String imageId) {
        byte[] img = getImage(imageId);
        if( img == null ){
            return null;
        }
        return new ByteArrayInputStream(getImage(imageId));
    }

    @Override
    public void save(String businessKey, String aspect, byte[] image, String mimeType, String info, String owner) {
    
        SimpleImage sim = getRepository().findAnyByImageKeyAndAspect(businessKey, aspect);
        
        if( sim == null ){
            sim = new SimpleImage();
        }
        
        sim.setAspect(aspect);
        sim.setImageKey(businessKey);
        sim.setImageId(UUID.randomUUID().toString());
        sim.setInfo(info);
        sim.setOwner(owner);
        sim.setMimeType(mimeType);
        sim.setContent(image);
        sim.setCreateDate(new Date());
        
        getRepository().save(sim);
    }

    @Override
    public void save(String businessKey, String aspect, InputStream image, String mimeType, String info, String owner) {
        try {
            byte[] data;
            data = IOUtils.toByteArray(image);
            
            
            save(businessKey, aspect, data, mimeType, info, owner);
            
        } catch (IOException ex) {
            LOG.error("Image cannot read", ex);
        }
    }

    @Override
    public void delete(String businessKey) {
        getRepository().removeByImageKey(businessKey);
    }

    @Override
    public void delete(String businessKey, String aspect) {
        getRepository().removeByImageKeyAnsAspect(businessKey, aspect);
    }

    @Override
    public boolean isImageExist(String businessKey) {
        return getRepository().countByImageKey(businessKey) > 0;
    }

    @Override
    public boolean isImageExist(String businessKey, String aspect) {
        return getRepository().countByImageKeyAndAspect(businessKey, aspect) > 0;
    }

    @Override
    public String getImageId(String businessKey, String aspect) {
        return getRepository().findImageIdByImageKeyAndAspect(businessKey, aspect);
    }


    
    
}
