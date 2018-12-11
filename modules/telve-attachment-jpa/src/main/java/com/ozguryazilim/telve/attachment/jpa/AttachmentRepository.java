/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment.jpa;

import com.ozguryazilim.telve.entities.Attachment;
import com.ozguryazilim.telve.entities.Attachment_;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 *
 * @author oyas
 */
public abstract class AttachmentRepository extends AbstractEntityRepository<Attachment, String> implements CriteriaSupport<Attachment>{

    private static final String FOLDER_MIME = "application/telve-folder";

    /**
     * Path bilgisi verilen folder'ı bulup döndürür.
     * @param path
     * @return 
     */
    public Attachment findFolderByPath( String path ){
        return criteria().eq(Attachment_.path, path)
                .eq(Attachment_.mimeType, FOLDER_MIME)
                .getOptionalResult();
        
    }

    
}
