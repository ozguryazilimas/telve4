/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.jcr.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.modeshape.common.text.UrlEncoder;
import org.modeshape.jcr.api.JcrTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Image storage'daki imajların jasper ropert içerisinden erişilebilmesi için yardımcı sınıf.
 *
 * Bu sınıf init edilip Jasper Report'a parametre olarak pass edilmeli.
 *
 * <parameter name="IMG_PROVIDER" class="com.ozguryazilim.telve.jcr.image.JasperReportImageProvider" isForPrompting="false"/>
 *
 * <image>
 *	<reportElement x="343" y="20" width="67" height="77" uuid="df00029a-3ad7-48e1-bf31-a9d8f5c28dcf"/>
 *      <imageExpression><![CDATA[$P{IMG_PROVIDER}.getImage($F{PHOTO_ID})]]></imageExpression>
 * </image>
 *
 * @author Hakan Uygun
 */
public class JasperReportImageProvider implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(JasperReportImageProvider.class);

    private String contextRoot;

    public JasperReportImageProvider(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    /**
     * Verilen bilgiler ile imaj içeriğinin id'sini döndürür.
     * @param keyValue
     * @param contextRoot
     * @return
     * @throws RepositoryException
     */
    public String getImageId( String keyValue ) throws RepositoryException{

        String folderName = contextRoot + "/";
        String path = folderName + buildPath(keyValue) + keyValue;

        JcrTools jcrTools = new JcrTools();

        UrlEncoder encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        String fileName = encoder.encode(path);

        Node node = getSession().getNode(fileName);

        return node.getIdentifier();
    }

    public BufferedImage getImage( String value ){
        //JCR'den content nodeu bulalım
        Node cn;
        BufferedImage img = null;
        try {
            cn = getImageContent( getImageId( value));
            img = ImageIO.read(cn.getProperty("jcr:data").getBinary().getStream());
        } catch (IOException ex) {
            LOG.error("IO Error", ex);
        } catch (RepositoryException ex) {
            LOG.debug("Repo Error", ex);
        }

        //TODO: Default image işine girmek lazım mı?

        return img;
    }

    public BufferedImage getImageById( String value ){
        //JCR'den content nodeu bulalım
        Node cn;
        BufferedImage img = null;
        try {
            cn = getImageContent(value);
            img = ImageIO.read(cn.getProperty("jcr:data").getBinary().getStream());
        } catch (IOException ex) {
            LOG.error("IO Error", ex);
        } catch (RepositoryException ex) {
            LOG.debug("Repo Error", ex);
        }

        //TODO: Default image işine girmek lazım mı?

        return img;
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
     * Geriye JCR instance'ını döndürür.
     * @return
     */
    protected Session getSession(){
        return BeanProvider.getContextualReference(Session.class, true);
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
}
