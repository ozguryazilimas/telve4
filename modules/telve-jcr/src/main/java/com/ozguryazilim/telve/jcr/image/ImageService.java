package com.ozguryazilim.telve.jcr.image;

import java.io.InputStream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @author Aydoğan Sel <aydogan.sel at iova.com.tr>>
 */
@Named
@ApplicationScoped
public class ImageService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    
    public InputStream getImage(String imageId) {
        LOG.debug("Requested Image : {}", imageId);
        InputStream is = null;
        try {
            Node cn = getImageContent(imageId); //Burada default bir imaj döndürmek lazım...
            is = cn.getProperty("jcr:data").getBinary().getStream();
        } catch (RepositoryException ex) {
            LOG.error("Image not found", ex);
        }

        return is;
    }
    
    public Node getImageContent(String id ) throws RepositoryException {
        try {
            Node node = getSession().getNodeByIdentifier( id );
            Node content = node.getNode("jcr:content");
            return content; 
        } catch (PathNotFoundException e) {
            LOG.debug("Imaj bulunamadı {}", id);
            throw e;
        }
    }
    
    
    /**
     * Geriye JCR Session instance'ı döndürür.
     *
     * @return
     */
    private Session getSession() {
        return BeanProvider.getContextualReference(Session.class);
    }
}
