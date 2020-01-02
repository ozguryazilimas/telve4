package com.ozguryazilim.telve.jcr.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.uploader.ui.FileUploadDialog;
import com.ozguryazilim.telve.uploader.ui.FileUploadHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.modeshape.common.text.UrlEncoder;
import org.modeshape.jcr.api.JcrTools;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JCR Arayüzü için control sınıfı.
 *
 * Kullanımı için Home beanler üzerinden init edilmesi gerekir.
 *
 * @author Hakan Uygun
 */
public class JcrController implements FileUploadHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JcrController.class);

    private String contextRoot;
    private String sourceDomain;
    private String sourceCaption;
    private Long sourceId;

    private String selectedPath;
    private String selectedId;
    private String newFolderName;
    private String viewMode = "widget";

    private List<Node> folders;
    private List<FileInfo> files;
    private Map<String, String> parentMap = new HashMap<>();
    private List<String> defaultFolders = new ArrayList<>();
    private Boolean showContextRoot = Boolean.TRUE;

    private UrlEncoder encoder;

    public JcrController(String contextRoot, String sourceDomain, String sourceCaption, Long sourceId) {

        encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        contextRoot = encoder.encode(contextRoot);

        this.contextRoot = contextRoot;
        this.sourceDomain = sourceDomain;
        this.sourceCaption = sourceCaption;
        this.sourceId = sourceId;
        this.selectedPath = contextRoot;
    }

    public JcrController(String contextRoot, String sourceDomain, String sourceCaption, Long sourceId, List<String> defaultFolders) {

        encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        contextRoot = encoder.encode(contextRoot);

        this.contextRoot = contextRoot;
        this.sourceDomain = sourceDomain;
        this.sourceCaption = sourceCaption;
        this.sourceId = sourceId;
        this.selectedPath = contextRoot;
        this.defaultFolders.addAll(defaultFolders);
    }

    public JcrController(String contextRoot, String sourceDomain, String sourceCaption, Long sourceId, List<String> defaultFolders, Boolean showContextRoot) {
        encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        contextRoot = encoder.encode(contextRoot);

        this.contextRoot = contextRoot;
        this.sourceDomain = sourceDomain;
        this.sourceCaption = sourceCaption;
        this.sourceId = sourceId;
        this.selectedPath = contextRoot;
        this.defaultFolders.addAll(defaultFolders);
        this.showContextRoot = showContextRoot;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public String getSourceDomain() {
        return sourceDomain;
    }

    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }

    public String getSourceCaption() {
        return sourceCaption;
    }

    public void setSourceCaption(String sourceCaption) {
        this.sourceCaption = sourceCaption;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public List<Node> getFolders() throws RepositoryException {
        if (folders == null) {

            populateFolders();
        }
        return folders;
    }

    private void populateFolders() throws RepositoryException {
        folders = new ArrayList<>();
        Session session = getSession();
        JcrTools jcrTools = new JcrTools();
        Node node = jcrTools.findOrCreateNode(session, getContextRoot(), "nt:folder");

        if (!defaultFolders.isEmpty()) {
            for (String df : defaultFolders) {
                Node dfnode = jcrTools.findOrCreateNode(session, getContextRoot() + "/" + encoder.encode(df), "nt:folder");
                LOG.debug("Default Folder Created {}", dfnode.getIdentifier());
                if (!showContextRoot) {
                    popuplateFolderNodes(dfnode, true);
                }
            }
            session.save();

            //Populate sonrası default selection
            if (!folders.isEmpty()) {
                setSelectedPath(folders.get(0).getPath());
            } else {
                setSelectedPath(getContextRoot());
            }
        }

        if (showContextRoot) {
            popuplateFolderNodes(node, true);
            setSelectedPath(getContextRoot());
        }
    }

    private void popuplateFolderNodes(Node node, boolean isRoot) throws RepositoryException {
        folders.add(node);
        parentMap.put(node.getIdentifier(), isRoot ? "#" : node.getParent().getIdentifier());
        NodeIterator it = node.getNodes();
        while (it.hasNext()) {
            Node n = it.nextNode();
            if (n.isNodeType("nt:folder")) {
                popuplateFolderNodes(n, false);
            }
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

    public String getParentIdentifier(String id) {
        return parentMap.get(id);
    }

    public List<FileInfo> getFiles() throws RepositoryException {
        if (files == null) {
            populateFiles();
        }

        return files;
    }

    /**
     * Buraya nt:file tipinde node gelir ve fileInfo döner.
     *
     * @param node
     */
    private FileInfo buildFileInfo(Node node) throws RepositoryException {
        FileInfo fm = new FileInfo();

        fm.setId(node.getIdentifier());
        fm.setName(encoder.decode(node.getName()));
        fm.setPath(encoder.decode(node.getPath()));

        fm.setCreateBy(node.getProperty("jcr:createdBy").getString());
        fm.setCreateDate(node.getProperty("jcr:created").getDate().getTime());

        if (node.isNodeType("tlv:ref")) {
            fm.setSourceDomain(node.getProperty("tlv:sourceDomain").getString());
            fm.setSourceCaption(node.getProperty("tlv:sourceCaption").getString());
            fm.setSourceId(node.getProperty("tlv:sourceId").getLong());

        }

        if (node.isNodeType("tlv:tag")) {

            if (node.hasProperty("tlv:info")) {
                fm.setInfo(node.getProperty("tlv:info").getString());
            }

            if (node.hasProperty("tlv:category")) {
                fm.setCategory(node.getProperty("tlv:category").getString());
            }
            //TODO: Taglar için sanırım array almak lazım
            //fm.setTags(node.getProperty("tlv:tags").getString());
        }

        Node cn = node.getNode("jcr:content");

        try {
            fm.setMimeType(cn.getProperty("jcr:mimeType").getString());
        } catch (Exception ex) {
            LOG.warn("MimeType not found", ex);
        }

        return fm;
    }

    private void populateFiles() throws RepositoryException {
        populateFiles(getSelectedPath());
    }

    private void populateFiles(String folder) throws RepositoryException {
        files = new ArrayList<>();

        try {
            Session session = getSession();
            JcrTools jcrTools = new JcrTools();
            Node node = jcrTools.findOrCreateNode(session, folder, "nt:folder");

            popuplateFileNodes(node);
        } catch (Exception ex) {
            LOG.error("Files cannot populate", ex);
        }
    }

    private void popuplateFileNodes(Node node) throws RepositoryException {

        NodeIterator it = node.getNodes();
        while (it.hasNext()) {
            Node n = it.nextNode();
            if (n.isNodeType("nt:file")) {
                files.add(buildFileInfo(n));
            }
        }
    }

    public void newFolder() throws RepositoryException {
        newFolder(newFolderName);
        newFolderName = "";
    }

    public void newFolder(String folderName) throws RepositoryException {
        LOG.info("Folder Name: {}", folderName);

        Session session = getSession();

        JcrTools jcrTools = new JcrTools();
        Node folder = jcrTools.findOrCreateNode(session, getSelectedPath() + "/" + encoder.encode(folderName), "nt:folder");

        //Bir şekilde parent id yanlış geliyor. Dolayısı ile ilk ekleme sırasında sorun çıkıyor o yüzden kayıt bittikten sonra folderların hepsini yeniden çekiyoruz.
        //folders.add(folder);
        //parentMap.put(folder.getIdentifier(), folder.getParent().getIdentifier());
        setSelectedPath(folder.getPath());
        setSelectedId(folder.getIdentifier());

        LOG.info("Folder Node: {}", folder);

        session.save();

        populateFolders();

    }

    public void handleFileUpload(FileUploadEvent event) throws RepositoryException {
        LOG.info("Uploaded File : {}", event.getFile().getFileName());

        String fileNamePath = event.getFile().getFileName();
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

        if (Strings.isNullOrEmpty(fileName)) {
            //FIXME: UI'a hata vermeli ama nasıl?
            return;
        }

        Session session = getSession();
        try {
            JcrTools jcrTools = new JcrTools();
            fileName = encoder.encode(fileName);

            LOG.debug("Encoded FileName : {}", fileName);
            String fullName = getContextRoot() + fileName;

            Node n = jcrTools.uploadFile(session, fullName, in);
            n.addMixin("tlv:ref");
            n.addMixin("tlv:tag");
            n.setProperty("tlv:sourceCaption", getSourceCaption());
            n.setProperty("tlv:sourceDomain", getSourceDomain());
            n.setProperty("tlv:sourceId", getSourceId());
            session.save();

            n.getProperty("jcr:createdBy").setValue(getUserId());
            session.save();

            LOG.debug("Dosya JCR'e kondu : {}", fullName);
        } catch (RepositoryException ex) {
            LOG.error("Reporsitory Exception", ex);
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        }
    }

    public void copyFile(String fileName, InputStream in) {

        if (Strings.isNullOrEmpty(fileName)) {
            //FIXME: UI'a hata vermeli ama nasıl?
            return;
        }

        Session session = getSession();

        try {
            JcrTools jcrTools = new JcrTools();

            fileName = encoder.decode(fileName);
            fileName = encoder.encode(fileName);
            Node n = jcrTools.uploadFile(session, fileName, in);

            n.addMixin("tlv:ref");
            n.addMixin("tlv:tag");
            n.setProperty("tlv:sourceCaption", getSourceCaption());
            n.setProperty("tlv:sourceDomain", getSourceDomain());
            n.setProperty("tlv:sourceId", getSourceId());

            session.save();

            n.getProperty("jcr:createdBy").setValue(getUserId());
            session.save();

            //View Modele de ekleyelim.
            FileInfo fi = buildFileInfo(n);
            if (!files.contains(fi)) {
                files.add(fi);
            }

            LOG.info("Dosya JCR'e kondu : {}", fileName);

        } catch (RepositoryException ex) {
            LOG.error("Reporsitory Exception", ex);
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        }

    }

    public String getSelectedPath() {
        return selectedPath;
    }

    public void setSelectedPath(String selectedPath) {
        this.selectedPath = selectedPath;
    }

    public String getSelectedId() throws RepositoryException {
        if (Strings.isNullOrEmpty(selectedId)) {
            Session session = getSession();
            Node node = session.getNode(getSelectedPath());
            selectedId = node.getIdentifier();
        }
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public void downloadFile(String id) throws RepositoryException {
        Session session = getSession();

        Node node = session.getNodeByIdentifier(id);

        Node content = node.getNode("jcr:content");

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        try {
            response.setContentType(content.getProperty("jcr:mimeType").getString());
        } catch (Exception ex) {
            LOG.warn("MimeType not found", ex);
        }

        response.setHeader("Content-disposition", "attachment;filename=" + node.getName());
        response.setContentLength((int) content.getProperty("jcr:data").getBinary().getSize());

        try {

            try (OutputStream out = response.getOutputStream()) {
                IOUtils.copy(content.getProperty("jcr:data").getBinary().getStream(), out);

                out.flush();
            }

            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            LOG.error("Error while downloading the file", ex);
            //facesMessages.add("Error while downloading the file: " + fileName + type );
        }
    }

    /**
     * Verilen mimeType için tanımlı icon path'ini döndürür.
     *
     * Bulamazsa generic bir icon path döndürür
     *
     * @param mimeType
     * @return
     */
    public String getIcon(String mimeType) {
        switch (mimeType) {
            case "application/pdf":
                return "fa-file-pdf-o";
            case "image/png":
                return "fa-file-image-o";
            case "image/jpg":
                return "fa-file-image-o";
            case "image/jpeg":
                return "fa-file-image-o";
            case "text/plain":
                return "fa-file-text-o";
            case "application/msword":
            case "application/vnd.oasis.opendocument.text":
                return "fa-file-word-o";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            case "application/vnd.ms-excel":
            case "application/vnd.oasis.opendocument.spreadsheet":
                return "fa-file-excel-o";

            default:
                return "fa-file-o";
        }
    }

    public void selectNode() throws RepositoryException {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("nodeId");
        Session session = getSession();

        Node node = session.getNodeByIdentifier(id);

        setSelectedPath(node.getPath());
        selectedId = id;
        populateFiles();
    }

    public String getNewFolderName() {
        return newFolderName;
    }

    public void setNewFolderName(String newFolderName) {
        this.newFolderName = newFolderName;
    }

    public void deleteFile(String id) throws RepositoryException {
        Session session = getSession();

        Node node = session.getNodeByIdentifier(id);

        node.remove();

        session.save();

        //View Modelden de silelim
        for (FileInfo m : files) {
            if (id.equals(m.getId())) {
                files.remove(m);
                break;
            }
        }
    }

    public void deleteFolder() throws RepositoryException {
        deleteFolder(getSelectedPath());
    }

    public void deleteFolder(String path) throws RepositoryException {
        Session session = getSession();

        Node node = session.getNode(path);
        node.remove();

        session.save();

        //View Modelden de silelim
        folders = null;
        parentMap.clear();
        setSelectedPath(getContextRoot());
        setSelectedId("");
        /*
         for (Node m : folders) {
         if (id.equals(m.getIdentifier())) {
         folders.remove(m);
         break;
         }
         }*/
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public Boolean getIsViewModeWidget() {
        return "widget".equals(viewMode);
    }

    /**
     * Geriye varsa kullanıcı adı döndürür.
     *
     * @return
     */
    protected String getUserId() {
        //FIXME: Subject ya da ActiveUserLookup bişi düzelmeli öncelikle
//        ActiveUserLookup aul = BeanProvider.getContextualReference(ActiveUserLookup.class, true);
//        if( aul != null && aul.getActiveUser() != null ){
//            return aul.getActiveUser().getLoginName();
//        }
        return "";
    }

    public UrlEncoder getEncoder() {
        return encoder;
    }

    @Override
    public void handleFileUpload(String uri) {
        try {
            UploadInfo uploadInfo = getFileUploadService().getUploadInfo(uri);
            LOG.debug("Uploaded File : {}", uploadInfo.getFileName());

            String folderName = getSelectedPath();
            String path = folderName + "/" + uploadInfo.getFileName();

            LOG.info("Folder Name : {}", path);
            copyFile(path, getFileUploadService().getUploadedBytes(uri));
            getFileUploadService().deleteUpload(uri);
        } catch (IOException | TusException ex) {
            LOG.error("Attachment cannot add", ex);
        }
    }

    /**
     * Bu Method FileUploadDialog'unu açmasını sağlar.
     */
    public void uploadDocument() {
        getFileUploadDialog().openDialog(this, "");
    }

    protected TusFileUploadService getFileUploadService() {
        return BeanProvider.getContextualReference(TusFileUploadService.class, true);
    }

    protected FileUploadDialog getFileUploadDialog() {
        return BeanProvider.getContextualReference(FileUploadDialog.class, true);
    }

    public InputStream getImage(String imageId) {
        LOG.debug("Requested Image : {}", imageId);
        InputStream is = null;
        try {
            is = getImageContent(imageId); //Burada default bir imaj döndürmek lazım...
        } catch (RepositoryException ex) {
            LOG.error("Image not found", ex);
        }

        return is;
    }

    public StreamedContent getImage() {
        LOG.info("Image getting");

        FacesContext context = FacesContext.getCurrentInstance();
        String fileId2 = context.getExternalContext().getRequestParameterMap().get("fileid");
        String name = context.getExternalContext().getRequestParameterMap().get("name");
        LOG.info("File id is :  " + fileId2);
        LOG.info("File name is :  " + name);

        for (FileInfo m : files) {
            LOG.info("File name is :  " + m.getName());
            LOG.info("File id is :  " + m.getId());
        }

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            //Long fileId = Long.parseLong(context.getExternalContext().getRequestParameterMap().get("fileId"));
            String fileId = context.getExternalContext().getRequestParameterMap().get("fileId");
            LOG.info("File is :  " + fileId);
            FileInfo fInfo = null;
            for (FileInfo m : files) {
                if (fileId.equals(m.getId())) {
                    LOG.info("Bulundu");
                    fInfo = m;
                    break;
                }
            }

            InputStream is = null;
            try {
                is = getImageContent(fInfo); //Burada default bir imaj döndürmek lazım...
            } catch (RepositoryException ex) {
                java.util.logging.Logger.getLogger(JcrController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Burada default bir imaj döndürmek lazım...
            if (is != null) {
                return new DefaultStreamedContent(is);
            } else {
                return null;
            }

        }
    }

    public InputStream getImageContent(FileInfo file) throws RepositoryException {
        return getImageContent(file.getId());
    }

    public InputStream getImageContent(String id) throws RepositoryException {
        try {
            Session session = getSession();
            Node node = session.getNodeByIdentifier(id);
            Node content = node.getNode("jcr:content");
            return content.getProperty("jcr:data").getBinary().getStream();
        } catch (PathNotFoundException e) {
            return null;
        }
    }

    public StreamedContent getDefaultImage() {
        return new DefaultStreamedContent(Thread.currentThread().getContextClassLoader().getResourceAsStream("/defaultImage.jpg"));
    }

}
