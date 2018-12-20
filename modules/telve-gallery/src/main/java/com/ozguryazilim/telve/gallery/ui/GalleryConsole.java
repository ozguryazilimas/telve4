package com.ozguryazilim.telve.gallery.ui;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.jcr.ui.FileInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.modeshape.common.text.UrlEncoder;
import org.modeshape.jcr.api.JcrTools;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gallery Console UI Control Class.
 *
 * Resim / İmaj galerisi için temel kontrolsınıfı.
 *
 * Bir klasör içerisinde tag ve kategory ayrımına göre sadece imaj toparlar.
 *
 * TODO: Pager yapılmalı. Sorgu sonucunda bütün imajlar dönüyor ve performans
 * problemi olabilir.
 *
 * TODO: İmajlar çok büyük olabilir. Bunlar için thumbnail imaj üretip onları da saklasak? Ama bu da erişim yöntemlerini yeniden kurgulamayı gerektirir. thumbnailator api'si kullanılabilir.
 * 
 * @author Hakan Uygun
 */
@Named
@WindowScoped
public class GalleryConsole extends AbstractGalleryController {

    private static final Logger LOG = LoggerFactory.getLogger(GalleryConsole.class);

    private static final String CONTEXT_ROOT = "/gallery";

    private List<FileInfo> files;

    //Her biri aslında context altında bir klasör oluşturacak. Varsayılan bir adet unsorted kategorisi olacak
    private List<String> galleries = new ArrayList<>();

    private String selectedGallery;
    private String searchText;

    private String selectedNodeId;
    private String selectedNodeName;
    private String selectedNodeInfo;
    private String selectedNodeTags;

    @PostConstruct
    public void init() {
        populateGalleries();
    }

    

    public void handleFileUpload(FileUploadEvent event) throws RepositoryException, UnsupportedEncodingException {
        LOG.info("Uploaded File : {}", event.getFile().getFileName());

        String fileNamePath = event.getFile().getFileName();
        //TODO: Buarada aslında böyle birşey yapmaya gerek olmaması lazım. Dosya adının UTF-8 alınabilmesi gerek.
        fileNamePath = new String(fileNamePath.getBytes("ISO-8859-1"));
        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf(File.separatorChar) + 1);

        try {

            String folderName = getSelectedPath();
            String path = folderName + "/" + fileName;

            LOG.info("Folder Name : {}", path);
            copyFile(path, event.getFile().getInputstream());

        } catch (IOException e) {
            LOG.error("IO Error", e);
        }
        //FacesMessage msg = new FacesMessage("Success! ", fileName + " is uploaded.");
        //FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * path'i ile birlikte verilen fileName'e verilen streami yükler.
     *
     * Path'in önüne contextRoot ekler.
     *
     * @param fileName
     * @param in
     */
    public void uploadFile(String fileName, InputStream in) {
        Session session = getSession();
        try {
            JcrTools jcrTools = new JcrTools();
            UrlEncoder encoder = new UrlEncoder();
            encoder.setSlashEncoded(false);
            fileName = encoder.encode(fileName);

            LOG.debug("Encoded FileName : {}", fileName);
            String fullName = fileName;

            Node n = jcrTools.uploadFile(session, fullName, in);
            n.addMixin("tlv:tag");
            session.save();
            //FIXME: Burada aktif kullanıcı adı UserLookup'dan alınacak. Eğer yoksa ( session yoksa yoktur ) anonim bırkılacak.
            //n.getProperty("jcr:createdBy").setValue("Hakan");
            //session.save();
            LOG.debug("Dosya JCR'e kondu : {}", fullName);
        } catch (RepositoryException ex) {
            LOG.error("Reporsitory Exception", ex);
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        }
    }

    public void copyFile(String fileName, InputStream in) {

        Session session = getSession();

        try {
            JcrTools jcrTools = new JcrTools();

            UrlEncoder encoder = new UrlEncoder();
            encoder.setSlashEncoded(false);
            fileName = encoder.encode(fileName);

            Node n = jcrTools.uploadFile(session, fileName, in);

            n.addMixin("tlv:tag");

            session.save();

            //FIXME: Burada aktif kullanıcı adı UserLookup'dan alınacak. Eğer yoksa ( session yoksa yoktur ) anonim bırkılacak.
            //n.getProperty("jcr:createdBy").setValue("Hakan");
            //session.save();
            //View Modele de ekleyelim.
            //files.add(buildFileInfo(n));

            LOG.info("Dosya JCR'e kondu : {}", fileName);

        } catch (RepositoryException ex) {
            LOG.error("Reporsitory Exception", ex);
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        }

    }

    
    /**
     * Verilen ID'li içeriği siler.
     * 
     * TODO: Burada kullanım kotrülü yapılmalı. Belki içerik üzerinde kullanım counterı tutulabilir ve ona bakılabilir.
     * 
     * @param id
     * @throws RepositoryException
     */
    public void deleteItem(String id) throws RepositoryException {
        Session session = getSession();
        Node node = session.getNodeByIdentifier(id);
        node.remove();
        session.save();
        search();
    }

    public void selectItem(String id) throws RepositoryException {
        Node node = getSession().getNodeByIdentifier(id);
        selectedNodeId = id;
        selectedNodeName = node.getName();
        if (node.hasProperty("tlv:info")) {
            selectedNodeInfo = node.getProperty("tlv:info").getString();
        } else {
            selectedNodeInfo = "";
        }

        if (node.hasProperty("tlv:tags")) {
            selectedNodeTags = Joiner.on(',').join(node.getProperty("tlv:tags").getValues());
        } else {
            selectedNodeTags = "";
        }

    }

    public void saveSelectedItem() throws RepositoryException {
        Session session = getSession();
        Node node = session.getNodeByIdentifier(selectedNodeId);
        node.setProperty("tlv:info", selectedNodeInfo);
        node.setProperty("tlv:tags", Splitter.on(',').trimResults().omitEmptyStrings().splitToList(selectedNodeTags).toArray(new String[]{}));
        session.save();
        selectedNodeId = "";
        selectedNodeInfo = "";
        selectedNodeTags = "";
        selectedNodeName = "";
    }

    public String getSelectedNodeId() {
        return selectedNodeId;
    }

    public void setSelectedNodeId(String selectedNodeId) {
        this.selectedNodeId = selectedNodeId;
    }

    public String getSelectedNodeName() {
        return selectedNodeName;
    }

    public void setSelectedNodeName(String selectedNodeName) {
        this.selectedNodeName = selectedNodeName;
    }

    public String getSelectedNodeInfo() {
        return selectedNodeInfo;
    }

    public void setSelectedNodeInfo(String selectedNodeInfo) {
        this.selectedNodeInfo = selectedNodeInfo;
    }

    public String getSelectedNodeTags() {
        return selectedNodeTags;
    }

    public void setSelectedNodeTags(String selectedNodeTags) {
        this.selectedNodeTags = selectedNodeTags;
    }

}
