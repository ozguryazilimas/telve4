/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment.jpa;

import com.ozguryazilim.telve.attachment.AttachmentContext;
import com.ozguryazilim.telve.attachment.AttachmentDocument;
import com.ozguryazilim.telve.attachment.AttachmentException;
import com.ozguryazilim.telve.attachment.AttachmentFolder;
import com.ozguryazilim.telve.attachment.AttachmentNotFoundException;
import com.ozguryazilim.telve.attachment.AttachmentStore;
import com.ozguryazilim.telve.entities.Attachment;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author oyas
 */
@Dependent
public class AttachmentJPAStore implements AttachmentStore{

    @Inject
    private AttachmentRepository repository;
    
    @Override
    public void start() throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop() throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> getCapabilities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentFolder getFolder(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException {
    
        //FIXME: Burada path contextRoot ile birleştirilmeli.
        
        Attachment attachment = repository.findFolderByPath(path);
        if( attachment == null ){
            throw new AttachmentNotFoundException();
        }
        
        AttachmentFolder result = new AttachmentFolder();
        result.setId(attachment.getId());
        result.setName(attachment.getName());
        result.setPath(attachment.getPath());
        result.setCreateBy(attachment.getCreateBy());
        result.setCreateDate(attachment.getCreateDate());
        
        return result;
    }

    @Override
    public List<AttachmentFolder> getFolders(AttachmentContext context) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentDocument getDocument(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentDocument getDocumentById(AttachmentContext context, String id) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AttachmentDocument> getDocuments(AttachmentContext context, AttachmentFolder folder) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AttachmentDocument> getDocuments(AttachmentContext context, String folder) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InputStream getDocumentContent(AttachmentContext context, String id) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentDocument addDocument(AttachmentContext context, AttachmentFolder folder, AttachmentDocument document) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentDocument addDocument(AttachmentContext context, AttachmentFolder folder, AttachmentDocument document, InputStream content) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentDocument addDocument(AttachmentContext context, AttachmentFolder folder, AttachmentDocument document, byte[] content) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentFolder addFolder(AttachmentContext context, AttachmentFolder parent, String name) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AttachmentFolder> getFolders(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttachmentFolder addFolder(AttachmentContext context, String path) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteDocument(AttachmentContext context, String id) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteFolder(AttachmentContext context, String path) throws AttachmentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
