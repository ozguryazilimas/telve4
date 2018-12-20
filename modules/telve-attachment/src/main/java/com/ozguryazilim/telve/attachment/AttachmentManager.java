package com.ozguryazilim.telve.attachment;

import com.ozguryazilim.telve.attachment.qualifiers.FileStoreLiteral;
import java.io.Serializable;
import java.util.List;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 *
 * @author Hakan Uygun
 */
public class AttachmentManager implements Serializable{
    
    private AttachmentContext context;
    private List<String> stores;
    List<AttachmentDocument> attachments;

    public AttachmentManager(AttachmentContext context) {
        this.context = context;
    }

    public AttachmentContext getContext() {
        return context;
    }

    public void setContext(AttachmentContext context) {
        this.context = context;
        stores.clear();
        stores = null;
        
        attachments.clear();
        attachments = null;
    }
    
    public List<AttachmentDocument> getDocuments() throws AttachmentNotFoundException, AttachmentException{
        if( attachments == null ){
            attachments = getStore().getDocuments(context, "");
        }
        return attachments;
    }
    
    protected AttachmentStore getStore(){
        AttachmentStore result = BeanProvider.getContextualReference(AttachmentStore.class, new FileStoreLiteral());
        return result;
    }
    
}
