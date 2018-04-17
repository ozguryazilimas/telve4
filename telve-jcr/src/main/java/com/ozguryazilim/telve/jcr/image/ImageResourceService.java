/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.jcr.image;

import java.io.InputStream;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fotolar için REST API.
 * 
 * Bu servis sayesinde galerideki imajlar normal imaj dosyaları olarak sunucudan istenebilir hale gelecekler.
 * 
 * TODO: Yetki kontrolü için picketlink bağlantısı yapılacak
 * 
 * @author Hakan Uygun
 */
@Path("/images")
public class ImageResourceService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ImageResourceService.class);
    
    @GET    
    @Path("/image/{id}")
    @Produces("image/*")
    public Response getImage( @PathParam("id") String id ) throws RepositoryException{
        
        //JCR'den content nodeu bulalım
        Node cn = getImageContent( id );
        
        //MimeType Mapping
        //String mt = cn.getProperty("jcr:mimeType").getString();//new MimetypesFileTypeMap().getContentType(cn.getProperty("jcr:mimeType").getString());
        
        //İçeriği yollayalım.
        return Response.ok(cn.getProperty("jcr:data").getBinary().getStream()).build();
        
    }
    
    @GET    
    @Path("/default/{id}")
    @Produces("image/*")
    public Response getDefaultImage( @PathParam("id") String id ) throws RepositoryException{
        
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + id );
        
        //MimeType Mapping
        String mt = "image/jpg";
        
        //İçeriği yollayalım.
        return Response.ok(is, mt).build();
        
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
