package com.ozguryazilim.telve.attachment.modeshape;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.ozguryazilim.telve.attachment.AttachmentContext;
import com.ozguryazilim.telve.attachment.AttachmentDocument;
import com.ozguryazilim.telve.attachment.AttachmentException;
import com.ozguryazilim.telve.attachment.AttachmentFolder;
import com.ozguryazilim.telve.attachment.AttachmentNotFoundException;
import com.ozguryazilim.telve.attachment.AttachmentStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.tika.io.IOUtils;
import org.modeshape.common.text.UrlEncoder;
import org.modeshape.jcr.api.JcrTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Embeded ModeShape ile AttachmentStore implementasyonu
 *
 * @author Hakan Uygun
 */
public class AttachmentModeShapeStore implements AttachmentStore {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentModeShapeStore.class);

    private UrlEncoder encoder;

    @Override
    public void start() throws AttachmentException {

        try {
            encoder = new UrlEncoder();
            encoder.setSlashEncoded(false);

            //Engine'de başlatılsın
            Session session = ModeShapeRepositoryFactory.getSession();
            session.logout();
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }

    }

    @Override
    public void stop() throws AttachmentException {
        ModeShapeRepositoryFactory.shutdown();
    }

    @Override
    public Set<String> getCapabilities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Verilen Path'deki folder'ı döndürür.
     *
     * ModeShape provider'ı eğer istenen folder yoksa da oluşturur.
     *
     * @param context
     * @param path
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException
     */
    @Override
    public AttachmentFolder getFolder(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException {

        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            String fullPath = getEncodedPath(getFullPath(context, path));

            JcrTools jcrTools = new JcrTools();
            Node node = jcrTools.findOrCreateNode(session, fullPath, "nt:folder");

            AttachmentFolder result = nodeToFolder(node);

            session.logout();

            return result;
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public List<AttachmentFolder> getFolders(AttachmentContext context) throws AttachmentNotFoundException, AttachmentException {
        return getFolders(context, "");
    }

    /**
     * ContextRoot altında bulunan folderları verir.
     *
     * @param context
     * @param path
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException
     */
    @Override
    public List<AttachmentFolder> getFolders(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException {

        List<AttachmentFolder> result = new ArrayList<>();

        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            String fullPath = getEncodedPath(getFullPath(context, path));

            JcrTools jcrTools = new JcrTools();
            Node node = jcrTools.findOrCreateNode(session, fullPath, "nt:folder");

            populateFolders(node, result);

            session.logout();

            return result;
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    protected void populateFolders(Node node, List<AttachmentFolder> list) throws RepositoryException {
        NodeIterator it = node.getNodes();
        while (it.hasNext()) {
            Node n = it.nextNode();
            if (n.isNodeType("nt:folder")) {
                AttachmentFolder folder = nodeToFolder(n);
                list.add(folder);
                //Şimdi alt folderlar.
                populateFolders(n, list);
            }
        }
    }

    @Override
    public AttachmentDocument getDocument(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            String fullPath = getEncodedPath(getFullPath(context, path));

            JcrTools jcrTools = new JcrTools();
            Node node = jcrTools.findOrCreateNode(session, fullPath, "nt:file");

            if (node.isNew()) {
                throw new AttachmentNotFoundException();
            }

            AttachmentDocument result = nodeToDocument(node);

            session.logout();

            return result;
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public AttachmentDocument getDocumentById(AttachmentContext context, String id) throws AttachmentNotFoundException, AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            Node node = session.getNodeByIdentifier(id);

            if (node.isNew()) {
                throw new AttachmentNotFoundException();
            }

            AttachmentDocument result = nodeToDocument(node);

            session.logout();

            return result;
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public List<AttachmentDocument> getDocuments(AttachmentContext context, AttachmentFolder folder) throws AttachmentNotFoundException, AttachmentException {
        return getDocuments(context, getFolderPath(context, folder));
    }

    @Override
    public List<AttachmentDocument> getDocuments(AttachmentContext context, String folder) throws AttachmentNotFoundException, AttachmentException {
        List<AttachmentDocument> result = new ArrayList<>();

        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            String folderPath = getEncodedPath(getFullPath(context, folder));

            JcrTools jcrTools = new JcrTools();
            Node node = jcrTools.findOrCreateNode(session, folderPath, "nt:folder");

            LOG.debug("Documents For Folder : {}", node.getPath());

            NodeIterator it = node.getNodes();
            while (it.hasNext()) {
                Node n = it.nextNode();
                if (n.isNodeType("nt:file")) {
                    AttachmentDocument doc = nodeToDocument(n);
                    result.add(doc);
                }
            }

            session.logout();

            return result;
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public List<AttachmentDocument> getDocumentsByTag(AttachmentContext context, String tag) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AttachmentDocument> getDocumentsByTags(AttachmentContext context, List<String> tags) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AttachmentDocument> getDocumentsByTags(AttachmentContext context, String... tag) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AttachmentDocument> getDocumentsByCategory(AttachmentContext context, String category) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InputStream getDocumentContent(AttachmentContext context, AttachmentDocument document) throws AttachmentNotFoundException, AttachmentException {
        return getDocumentContent(context, document.getId());
    }

    @Override
    public InputStream getDocumentContent(AttachmentContext context, String id) throws AttachmentNotFoundException, AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();
            Node node = session.getNodeByIdentifier(id);

            LOG.debug("Document Content Requested: {}", node.getPath());

            Node content = node.getNode("jcr:content");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            IOUtils.copy(content.getProperty("jcr:data").getBinary().getStream(), bos);

            session.logout();

            ByteArrayInputStream result = new ByteArrayInputStream(bos.toByteArray());

            return result;

        } catch (RepositoryException | IOException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public AttachmentDocument addDocument(AttachmentContext context, AttachmentFolder folder, AttachmentDocument document) throws AttachmentException {
        //TODO: UTF-8 charset vermeli mi?
        return addDocument(context, folder, document, document.getContent().getBytes());
    }

    @Override
    public AttachmentDocument addDocument(AttachmentContext context, AttachmentFolder folder, AttachmentDocument document, InputStream content) throws AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();
            JcrTools jcrTools = new JcrTools();

            //Burada sadece path'mi yoksa isim de mi işe girecek?
            String fullPath = getEncodedPath(getFullPath(context, getFolderPath(context, folder) + "/" + document.getName()));

            Node n = jcrTools.uploadFile(session, fullPath, content);

            /* FIXME: Buradan emin değilim.
            n.addMixin("tlv:ref");
            n.addMixin("tlv:tag");
            n.setProperty("tlv:sourceCaption", document.getFeature());
            n.setProperty("tlv:sourceDomain", document.getFeatureBK());
            n.setProperty("tlv:sourceId", document.getFeaturePK());
             */
            session.save();

            n.getProperty("jcr:createdBy").setValue(context.getUsername());
            session.save();

            LOG.debug("Document Added : {}", n.getPath());

            AttachmentDocument result = result = nodeToDocument(n);

            session.logout();

            return result;
        } catch (RepositoryException | IOException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public AttachmentDocument addDocument(AttachmentContext context, AttachmentFolder folder, AttachmentDocument document, byte[] content) throws AttachmentException {

        ByteArrayInputStream bis = new ByteArrayInputStream(content);
        return addDocument(context, folder, document, bis);

    }

    @Override
    public void deleteDocument(AttachmentContext context, String id) throws AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            Node node = session.getNodeByIdentifier(id);

            LOG.debug("Document delete : {}", node.getPath());

            node.remove();

            session.save();
            session.logout();

        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public AttachmentFolder addFolder(AttachmentContext context, AttachmentFolder parent, String name) throws AttachmentException {
        return addFolder(context, parent.getPath() + "/" + name);
    }

    @Override
    public AttachmentFolder addFolder(AttachmentContext context, String path) throws AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            String fullPath = getEncodedPath(getFullPath(context, path));

            JcrTools jcrTools = new JcrTools();
            Node node = jcrTools.findOrCreateNode(session, fullPath, "nt:folder");

            LOG.debug("Folder added : {}", node.getPath());

            session.save();

            AttachmentFolder result = nodeToFolder(node);

            session.logout();

            return result;
        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    @Override
    public void deleteFolder(AttachmentContext context, String path) throws AttachmentException {
        try {
            Session session = ModeShapeRepositoryFactory.getSession();

            Node node = session.getNode(getEncodedPath(getFullPath(context, path)));
            LOG.debug("Folder delete : {}", node.getPath());

            node.remove();

            session.save();
            session.logout();

        } catch (RepositoryException ex) {
            throw new AttachmentException();
        }
    }

    //////////////////////////////////////////
    //Util Functions
    /**
     * Türkçe ya da path'de kabul edilmeyecek karakterler temizleniyor
     *
     * @param path
     * @return
     */
    protected String getEncodedPath(String path) {
        return encoder.encode(path);
    }

    /**
     * Orjinal haline geri çevriliyor
     *
     * @param path
     * @return
     */
    protected String getDecodedPath(String path) {
        return encoder.decode(path);
    }

    /**
     * Geriye context ile birleşmiş path döner.
     *
     * @param context
     * @param path
     * @return
     */
    protected String getFullPath(AttachmentContext context, String path) {
        //FIXME: burada boşluk fazla slah v.s. kontrol edilecek.
        String s = context.getRoot() + "/" + path;

        //Önce parse edip bir parçalıyoruz
        List<String> ss = Splitter.on('/').trimResults().omitEmptyStrings().splitToList(s);
        //Şimdi de geri birleştiriyoruz
        s = Joiner.on('/').join(ss);

        return s;
    }

    /**
     * Folder Path içinde contextRoot varsa çıkarır.
     * @param context
     * @param folder
     * @return 
     */
    protected String getFolderPath( AttachmentContext context, AttachmentFolder folder ){
        //Context root sonunda içinde fazladan slash olabiliyor.
        String con = context.getRoot();
        if( con.endsWith("/") ){
            con = con.substring(0, con.length() - 1 );
        }
        
        //folder path içinden context root kısmını attık.
        return folder.getPath().replace(con, "");
    }

    protected AttachmentFolder nodeToFolder(Node node) throws RepositoryException {
        AttachmentFolder result = new AttachmentFolder();
        result.setId(node.getIdentifier());
        result.setPath(getDecodedPath(node.getPath()));
        result.setName(getDecodedPath(node.getName()));
        result.setParentId(node.getParent().getIdentifier());
        return result;
    }

    private AttachmentDocument nodeToDocument(Node node) throws RepositoryException {

        AttachmentDocument result = new AttachmentDocument();

        result.setId(node.getIdentifier());
        result.setName(getDecodedPath(node.getName()));
        result.setPath(getDecodedPath(node.getPath()));

        result.setCreateBy(node.getProperty("jcr:createdBy").getString());
        result.setCreateDate(node.getProperty("jcr:created").getDate().getTime());

        /* FIXME: Buraya ayrıca bakacağız
        if (node.isNodeType("tlv:ref")) {
            result.setFeature(node.getProperty("tlv:sourceDomain").getString());
            result.setFeatureBK(node.getProperty("tlv:sourceCaption").getString());
            result.setFeaturePK(node.getProperty("tlv:sourceId").getLong());

        }

        if (node.isNodeType("tlv:tag")) {

            if (node.hasProperty("tlv:info")) {
                result.setInfo(node.getProperty("tlv:info").getString());
            }

            if (node.hasProperty("tlv:category")) {
                result.setCategory(node.getProperty("tlv:category").getString());
            }
            //FIXME: Taglar için sanırım array almak lazım ve önemli.
            //fm.setTags(node.getProperty("tlv:tags").getString());
        }
         */
        Node cn = node.getNode("jcr:content");

        result.setMimeType(cn.getProperty("jcr:mimeType").getString());

        return result;
    }

}
