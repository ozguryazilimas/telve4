package com.ozguryazilim.telve.attachment;

/**
 * Bir Attachment Store'un neler yapabildiği
 * @author Hakan Uygun
 */
public enum AttachmentStoreCapability {
    
    /**
     * Eğer bir store writable değil ise ReadOnly'dir.
     */
    Writable,
    /**
     * Sorgulanabilir.
     * CMIS/JCR Query gibi 
     */
    Searchable,
    /**
     * Tag destekliyor
     */
    TagSupport,
    /**
     * Category destekliyor
     */
    CategorySupport,
    /**
     * Metadata destekliyor
     */
    MetadataSupport,
    /**
     * Embeded bir store'mu yoksa ağ üzerinden mi erişiliyor
     */
    Embeded,
    /**
     * Belgeler versionlanabiliyor mu?
     */
    VersionSupport
    
    
}
