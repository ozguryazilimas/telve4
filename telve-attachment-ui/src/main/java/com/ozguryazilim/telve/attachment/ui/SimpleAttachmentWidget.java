/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment.ui;

import com.ozguryazilim.telve.attachment.AttachmentContext;
import com.ozguryazilim.telve.attachment.AttachmentContextProvider;
import com.ozguryazilim.telve.attachment.AttachmentDocument;
import com.ozguryazilim.telve.attachment.AttachmentException;
import com.ozguryazilim.telve.attachment.AttachmentFolder;
import com.ozguryazilim.telve.attachment.AttachmentNotFoundException;
import com.ozguryazilim.telve.attachment.AttachmentStore;
import com.ozguryazilim.telve.attachment.AttacmentContextProviderSelector;
import com.ozguryazilim.telve.attachment.qualifiers.FileStore;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.FeaturePointer;
import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
@Named
@GroupedConversationScoped
public class SimpleAttachmentWidget implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleAttachmentWidget.class);

    @Inject
    private Identity identity;

    @Inject
    @FileStore
    private AttachmentStore store;

    @Inject
    private AttacmentContextProviderSelector providerSelector;

    private FeaturePointer featurePointer;
    
    private AttachmentContext context;
    private AttachmentFolder folder;
    private List<AttachmentDocument> documents;
    private Object payload;

    public void init(FeaturePointer featurePointer, Object payload, AttachmentContext context ) throws AttachmentNotFoundException, AttachmentException {
        this.featurePointer = featurePointer;
        this.payload = payload;
                
        if( context != null ){
            //Verilmiş ise providerdan al
            this.context = context;
        } else {
            //Verilmemiş kendimiz oluşturalım
            AttachmentContextProvider provider = providerSelector.getAttachmentContextProvider(featurePointer, payload );
            
            this.context = provider.getContext(featurePointer,payload);
        }
        
        folder = store.getFolder(this.context, "");
        clearChache();
    }

    public List<AttachmentDocument> getDocuments() {
        try {
            if( documents == null ){
                documents = store.getDocuments(context, "");
            }
            return documents;
        } catch (AttachmentNotFoundException | AttachmentException ex) {
            LOG.error("Attachmet Store Error", ex);
            FacesMessages.error("Attachment'lar alınamadı!"); //i18n
            return Collections.emptyList();
        }

    }


    public void deleteDocument(String id) throws AttachmentException {
        store.deleteDocument(context, id);
        clearChache();
    }

    public void downloadDocument(String id) throws AttachmentNotFoundException, AttachmentException, IOException {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        AttachmentDocument doc = store.getDocumentById(context, id);

        response.setContentType(doc.getMimeType());

        response.setHeader("Content-disposition", "attachment;filename=" + doc.getName());
        //response.setContentLength((int) content.getProperty("jcr:data").getBinary().getSize());

        try (OutputStream out = response.getOutputStream()) {
            IOUtils.copy(store.getDocumentContent(context, doc), out);

            out.flush();
        }

        FacesContext.getCurrentInstance().responseComplete();

    }

    /**
     * UI'dan gelen FileUpload dialoğunu karşılar.
     *
     * FIXME: Exception Handling
     *
     * @param event
     * @throws AttachmentException
     * @throws IOException
     */
    public void handleFileUpload(FileUploadEvent event) throws AttachmentException, IOException {
        LOG.debug("Uploaded File : {}", event.getFile().getFileName());

        String fileNamePath = event.getFile().getFileName();
        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf(File.separatorChar) + 1);

        LOG.debug("File Name : {}", fileName);
        store.addDocument(context, folder, new AttachmentDocument(fileName), event.getFile().getInputstream());
        clearChache();
    }

    protected void clearChache(){
        if( documents == null ) return;
        
        documents.clear();
        documents = null;
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
}
