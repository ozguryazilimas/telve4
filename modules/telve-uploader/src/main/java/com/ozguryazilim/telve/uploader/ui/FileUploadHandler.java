package com.ozguryazilim.telve.uploader.ui;

/**
 * FileUploadDialog için aslında callback interface'i.
 * 
 * 
 * 
 * 
 * @author Hakan Uygun
 */
public interface FileUploadHandler {
 
    /**
     * Bu method içerisinde yüklenmiş olan dosya URL'i gelir ve TusFileUploadService üzerinden yüklenen dosya işlenir. 
     * @param uri 
     */
    void handleFileUpload( String uri );

    /**
     * Upload with check zip files (if decompress true, zip is automatically decompress and extract same directory)
     * @param uri
     * @param decompress
     */
    void handleFileUpload( String uri, boolean decompress);
    
}
