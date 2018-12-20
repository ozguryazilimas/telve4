package com.ozguryazilim.telve.gallery.ui;

import com.google.common.base.Joiner;
import com.ozguryazilim.telve.gallery.GalleryRegistery;
import com.ozguryazilim.telve.jcr.ui.FileInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.modeshape.common.text.UrlEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GalleryConsole ve GalleryLookup için temel kontrol sınıfı.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractGalleryController implements Serializable{
   
    private static final Logger LOG = LoggerFactory.getLogger(AbstractGalleryController.class);
    
    private static final String CONTEXT_ROOT = "/gallery";
    
    private List<FileInfo> files;

    //Her biri aslında context altında bir klasör oluşturacak. Varsayılan bir adet unsorted kategorisi olacak
    private List<String> galleries = new ArrayList<>();

    private String selectedGallery;
    private String searchText;
    
    
    protected void populateGalleries() {
        galleries = GalleryRegistery.getGalleries();
        
        selectedGallery = galleries.get(0);
    }

    
    public List<FileInfo> getFiles() throws RepositoryException {
        if (files == null) {
            search();
        }

        return files;
    }

    /**
     * Buraya nt:file tipinde node gelir ve fileInfo döner.
     *
     * @param node
     */
    protected FileInfo buildFileInfo(Node node) throws RepositoryException {
        FileInfo fm = new FileInfo();

        fm.setId(node.getIdentifier());
        
        UrlEncoder encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        
        fm.setName(encoder.decode( node.getName()));
        fm.setPath(node.getPath());

        fm.setCreateBy(node.getProperty("jcr:createdBy").getString());
        fm.setCreateDate(node.getProperty("jcr:created").getDate().getTime());

        if (node.isNodeType("tlv:tag")) {

            if (node.hasProperty("tlv:info")) {
                fm.setInfo(node.getProperty("tlv:info").getString());
            }

            if (node.hasProperty("tlv:category")) {
                fm.setCategory(node.getProperty("tlv:category").getString());
            }

            if (node.hasProperty("tlv:tags")) {
                fm.setTags(Joiner.on(',').join(node.getProperty("tlv:tags").getValues()));
            }
        }

        Node cn = node.getNode("jcr:content");

        fm.setMimeType(cn.getProperty("jcr:mimeType").getString());

        return fm;
    }
    
    /**
     * Geriye JCR Session instance'ı döndürür.
     *
     * @return
     */
    protected Session getSession() {
        return BeanProvider.getContextualReference(Session.class);
    }

    
    public String getSelectedPath() {
        return CONTEXT_ROOT + "/" + getSelectedGallery();
    }

    public String getContextRoot() {
        return CONTEXT_ROOT;
    }

    
    public List<String> getGalleries() {
        return galleries;
    }

    public String getSelectedGallery() {
        return selectedGallery;
    }

    public void setSelectedGallery(String selectedGallery) {
        files = null;
        this.selectedGallery = selectedGallery;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void search() throws RepositoryException {
        //TODO: JCR-SQL ile arama yapılacak.
        if (files == null) {
            files = new ArrayList<>();
        } else {
            files.clear();
        }

        UrlEncoder encoder = new UrlEncoder();
        encoder.setSlashEncoded(false);
        
        QueryManager qm = getSession().getWorkspace().getQueryManager();
        
        String encodedSearchText = encoder.encode(getSearchText());
        
        String sql = "SELECT * FROM [nt:file] as t WHERE ISDESCENDANTNODE( ['" + getSelectedPath() + "'] ) AND ( name(t) like '%" + encodedSearchText + "%' OR t.[tlv:tags] like '%" + getSearchText() + "%' ) ";

        Query query = qm.createQuery(sql, Query.JCR_SQL2);
        QueryResult result = query.execute();
        NodeIterator it = result.getNodes();
        while (it.hasNext()) {
            Node n = it.nextNode();
            if (n.isNodeType("nt:file")) {
                files.add(buildFileInfo(n));
            }
        }
    }

}
