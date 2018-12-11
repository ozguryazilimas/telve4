/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
}
