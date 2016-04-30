/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.jcr.image;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import org.modeshape.common.text.UrlEncoder;
import org.modeshape.jcr.api.JcrTools;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Image yönetim için özelleşmiş JCR controller.
 *
 * inputLookupImage bileşeni tarafından kullanılır.
 *
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class ImageLookupController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ImageLookupController.class);

    @Inject
    private Session session;

    private String keyValue;
    private String contextRoot;

    /**
     * Verilen bilgiler ile imaj içeriğinin id'sini döndürür.
     *
     * @param keyValue
     * @param contextRoot
     * @return
     * @throws RepositoryException
     */
    public String getImageId(String keyValue, String contextRoot) throws RepositoryException {

        String folderName = contextRoot + "/";
        String path = folderName + buildPath(keyValue) + keyValue;

        JcrTools jcrTools = new JcrTools();

        UrlEncoder encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        String fileName = encoder.encode(path);

        Node node = session.getNode(fileName);

        return node.getIdentifier();
    }

    /**
     * Geriye Rest ile çakilecek imaj path'ini döndürür.
     *
     * @param keyValue
     * @param contextRoot
     * @param emptyImage eğer imaj bulunamaz ise default ne döndürülecek?
     * @return
     */
    public String getImageResourcePath(String keyValue, String contextRoot, String emptyImage) {
        try {
            return "/rest/images/image/" + getImageId(keyValue, contextRoot);
        } catch (RepositoryException ex) {
            LOG.debug("Hata var:", ex);
        }

        return "/rest/images/default/" + emptyImage;
    }

    /**
     * Geriye Rest ile çakilecek imaj path'ini döndürür.
     *
     * eğer verilen key için imaj bulunamaz ise fallbackImage için kontrol
     * yapılır. O da bulunamaz ise emptyImage döndürülür.
     *
     * @param keyValue
     * @param contextRoot
     * @param fallbackImage eğer asıl imaj bulunamaz ise buna bakılacak bu da
     * bulunamaz ise emptyImage'a bakılacak
     * @param emptyImage eğer imaj bulunamaz ise default ne döndürülecek?
     * @return
     */
    public String getImageResourcePath(String keyValue, String contextRoot, String fallbackImage, String emptyImage) {
        try {
            return "/rest/images/image/" + getImageId(keyValue, contextRoot);
        } catch (PathNotFoundException ex) {
            LOG.debug("Hata var:", ex);
        } catch (RepositoryException ex) {
            LOG.debug("Hata var:", ex);
        }

        if (!Strings.isNullOrEmpty(fallbackImage)) {
            try {
                return "/rest/images/image/" + getImageId(fallbackImage, contextRoot);
            } catch (PathNotFoundException ex) {
                LOG.debug("Hata var:", ex);    
            } catch (RepositoryException ex) {
                LOG.debug("Hata var:", ex);
            }
        }

        return "/rest/images/default/" + emptyImage;
    }

    /**
     * Verilen bilgilere sahip bir imaj var mı bilgisini döndürür.
     * 
     * Örnek : imageLookupController.hasImage( "727272-828282", "/deneme/images" );
     * 
     * @param keyValue
     * @param contextRoot
     * @return 
     */
    public Boolean hasImage( String keyValue, String contextRoot ){
        
        try {
            String s = getImageId(keyValue, contextRoot);
            if( !Strings.isNullOrEmpty(s)){
                return true;
            }
        } catch (PathNotFoundException ex) {
            LOG.debug("Hata var:", ex);
        } catch (RepositoryException ex) {
            LOG.debug("Hata var:", ex);
        }
        
        return false;
    }
    
    
    /**
     * Upload işlemi başlamadan önce gerekli değişkenleri düzenler.
     *
     * @param keyValue
     * @param contextRoot
     */
    public void prepareToUpload(String keyValue, String contextRoot) {
        this.contextRoot = contextRoot;
        this.keyValue = keyValue;
    }

    /**
     * Upload işlemi için PF upload bileşeninin henadler'ı
     *
     * @param event
     * @throws RepositoryException
     */
    public void handleFileUpload(FileUploadEvent event) throws RepositoryException {
        LOG.info("Uploaded File : {}", event.getFile().getFileName());

        deleteImage( keyValue, contextRoot );
        
        String fileNamePath = event.getFile().getFileName();
        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf(File.separatorChar) + 1);

        try {

            String folderName = contextRoot + "/";
            String path = folderName + buildPath(keyValue) + keyValue;

            LOG.info("Folder Name : {}", path);
            copyFile(path, event.getFile().getInputstream());

        } catch (IOException e) {
            LOG.error("IO Error", e);
        }
        //FacesMessage msg = new FacesMessage("Success! ", fileName + " is uploaded.");
        //FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Asıl veri yükleme işini yapar.
     *
     * PF upload'dan gelen dosyayı JCR'ye yükler.
     *
     * @param fileName
     * @param in
     */
    protected void copyFile(String fileName, InputStream in) {

        try {
            JcrTools jcrTools = new JcrTools();

            UrlEncoder encoder = new UrlEncoder();
            encoder.setSlashEncoded(false);
            fileName = encoder.encode(fileName);

            Node n = jcrTools.uploadFile(session, fileName, in);

            LOG.info("Session root : {}", session.getRootNode());

            session.save();

            LOG.info("Dosya JCR'e kondu : {}", fileName);

        } catch (RepositoryException ex) {
            LOG.error("Reporsitory Exception", ex);
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        }

    }

    /**
     * URL olarak verilmiş dosyayı JCR'ye yükler.
     *
     * @param fileName
     * @param fileURL
     */
    protected void copyFileURL(String fileName, URL fileURL) {

        try {
            JcrTools jcrTools = new JcrTools();
            UrlEncoder encoder = new UrlEncoder();
            Node n = jcrTools.uploadFile(session, fileName, fileURL);

            session.save();

            LOG.info("Dosya JCR'e kondu : {}", fileName);

        } catch (RepositoryException ex) {
            LOG.error("Reporsitory Exception", ex);
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        }

    }

    /**
     * Çok fazla dosya olması durumunu kontrol etmek için.
     *
     * Şimdilikkapalı aslında
     *
     * @param tckn
     * @return
     */
    protected String buildPath(String tckn) {

        return "";
        /* Eğer performans problemi yaşanırsa kullanmak lazım : 
        StringBuffer sb = new StringBuffer();
        for (char c : tckn.toCharArray()) {
            sb.append(c).append('/');
        }

        return sb.toString();
         */
    }
    
    public void deleteImage(String keyValue, String contextRoot) throws RepositoryException{
        String folderName = contextRoot + "/";
        String path = folderName + buildPath(keyValue) + keyValue;

        UrlEncoder encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        String fileName = encoder.encode(path);

        LOG.debug("Item Remove Path : {}", fileName);
        
        try {
            //Node node = session.getNode(fileName);
            session.removeItem(fileName);
            session.save();
        } catch (LockException | ConstraintViolationException | AccessDeniedException ex) {
            throw new RepositoryException(ex);
        } catch (RepositoryException ex) {
            //Silinecek dosya bulunaması bir hata olarak fılatılmayacak.
            LOG.debug("File Not found", ex );
        }
        
    }
}
