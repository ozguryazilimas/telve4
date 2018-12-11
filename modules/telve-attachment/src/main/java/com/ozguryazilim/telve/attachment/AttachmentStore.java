/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.attachment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Attachment Store yazanlar için SPI
 * 
 * @author Hakan Uygun
 */
public interface AttachmentStore extends Serializable{
    
    /**
     * Store'un init olması için çağrılır.
     * 
     * @throws AttachmentException 
     */
    void start() throws AttachmentException;
    
    /**
     * Store'un kapatılması için çağrılır.
     * 
     * @throws AttachmentException 
     */
    void stop() throws AttachmentException;
    
    /**
     * Geriye store'un yapabileceklerinin listesini döndürür.
     * 
     * Burdan gelen bilgiye göre iş akışlarında store seçilebilir.
     * @return 
     */
    Set<String> getCapabilities();
    
    /**
     * Verilen PATH'e uygun düşen folder bulunup döndürülür.
     * 
     * Eğer bulunamaz ise AttachmentNotFoundException fırlatılır.
     * Başka türlü her hata içi AttachmentException fırlatılır.
     * 
     * @param context
     * @param path
     * @return
     * @throws com.ozguryazilim.telve.attachment.AttachmentNotFoundException
     * @throws AttachmentException 
     */
    AttachmentFolder getFolder( AttachmentContext context, String path ) throws AttachmentNotFoundException, AttachmentException;
    
    /**
     * AttachmentContext içinde tanımlanmış contextRoot kullanılarak bütün folderlar döner.
     * 
     * Eğer contextRoot yok ise AttachmentNotFoundException diğer hallerde AttachmentException fırlatılır.
     * 
     * @param context
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    List<AttachmentFolder> getFolders( AttachmentContext context ) throws AttachmentNotFoundException, AttachmentException;
    
    /**
     * AttachmentContext içinde tanımlanmış contextRoot kullanılarak bütün folderlar döner.
     * 
     * Eğer contextRoot yok ise AttachmentNotFoundException diğer hallerde AttachmentException fırlatılır.
     * 
     * @param context
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    List<AttachmentFolder> getFolders(AttachmentContext context, String path) throws AttachmentNotFoundException, AttachmentException;

    /**
     * Verilen PATH'deki document döner.
     * 
     * @param context
     * @param path
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    AttachmentDocument getDocument( AttachmentContext context, String path ) throws AttachmentNotFoundException, AttachmentException;
    
    /**
     * Verilen ID'ye sahip belge döner.
     * 
     * @param context
     * @param id
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    AttachmentDocument getDocumentById( AttachmentContext context, String id ) throws AttachmentNotFoundException, AttachmentException;
    
    /**
     * Verilen folder içindeki tüm belgeleri döndürür.
     * 
     * Eğer folder bulunamaz ise AttachmentNotFoundException fırlatılır.
     * 
     * @param context
     * @param folder
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    List<AttachmentDocument> getDocuments( AttachmentContext context, AttachmentFolder folder ) throws AttachmentNotFoundException, AttachmentException;

    /**
     * Verilen folder path içindeki tüm belgeleri döndürür.
     * 
     * Eğer folder bulunamaz ise AttachmentNotFoundException fırlatılır.
     * 
     * @param context
     * @param folder
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    List<AttachmentDocument> getDocuments( AttachmentContext context, String folder ) throws AttachmentNotFoundException, AttachmentException;
    
    /**
     * Verilen tag'a sahip bütün belgeler döndürürlür.
     * 
     * eğer bulunamaz ise emptyList döner.
     * 
     * @param context
     * @param tag
     * @return
     * @throws AttachmentException 
     */
    List<AttachmentDocument> getDocumentsByTag( AttachmentContext context, String tag ) throws AttachmentException;

    /**
     * Verilen tagların hepsine sahip belgelerin listesi döndürülür.
     * 
     * eğer bulunamaz ise emptyList döner.
     * 
     * @param context
     * @param tags
     * @return
     * @throws AttachmentException 
     */
    List<AttachmentDocument> getDocumentsByTags( AttachmentContext context, List<String> tags ) throws AttachmentException;

    /**
     * Verilen tagların hepsine sahip belgelerin listesi döndürülür.
     * 
     * eğer bulunamaz ise emptyList döner.
     * 
     * @param context
     * @param tag
     * @return
     * @throws AttachmentException 
     */
    List<AttachmentDocument> getDocumentsByTags( AttachmentContext context, String... tag ) throws AttachmentException;

    /**
     * Verilen category'e sahip belgelerin listesi döndürülür.
     * 
     * eğer bulunamaz ise emptyList döner.
     * 
     * @param context
     * @param category
     * @return
     * @throws AttachmentException 
     */
    List<AttachmentDocument> getDocumentsByCategory( AttachmentContext context, String category ) throws AttachmentException;    
    
    /**
     * Verilen belge için tanımlı binary contenti döndürür.
     * 
     * Eğer belge bulunamaz ise AttachmentNotFoundException fırlatılır.
     * 
     * @param context
     * @param document
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    InputStream getDocumentContent( AttachmentContext context, AttachmentDocument document ) throws AttachmentNotFoundException, AttachmentException;

    /**
     * Verilen ID'ye sahip belge için binary contenti döndürür.
     * 
     * Eğer belge bulunamaz ise AttachmentNotFoundException fırlatılır.
     * 
     * @param context
     * @param id
     * @return
     * @throws AttachmentNotFoundException
     * @throws AttachmentException 
     */
    InputStream getDocumentContent( AttachmentContext context, String id ) throws AttachmentNotFoundException, AttachmentException;
    
    /**
     * Bu method aslında Link v.b. kullanmak için kullanılabilir sadece. Binary verisi olmadan pek bir anlamı yok.
     * 
     * O yüzden pek emin değilim. CMIS ve JCR API'lerine bakalım geriye id döndürebilir miyiz? CMIS'da dönüyor.
     * 
     * Geriye yani bir model ( parametreye verilen değil ) AttachmentDocument döner.
     * 
     * @param context
     * @param folder
     * @param document 
     * @return  
     * @throws com.ozguryazilim.telve.attachment.AttachmentException  
     */
    AttachmentDocument addDocument( AttachmentContext context, AttachmentFolder folder, AttachmentDocument document) throws AttachmentException;
    
    /**
     * Bu method ile verilen folder'a yeni bir document eklenir.
     * 
     * Binary veri inputStream olarak verilir.
     * 
     * Geriye yani bir model ( parametreye verilen değil ) AttachmentDocument döner.
     * 
     * @param context
     * @param folder
     * @param document 
     * @param content 
     * @return  
     * @throws com.ozguryazilim.telve.attachment.AttachmentException  
     */
    AttachmentDocument addDocument( AttachmentContext context, AttachmentFolder folder, AttachmentDocument document, InputStream content) throws AttachmentException;
    
    /**
     * Bu method ile verilen folder'a yeni bir document eklenir.
     * 
     * Binary veri inputStream olarak verilir.
     * 
     * Geriye yani bir model ( parametreye verilen değil ) AttachmentDocument döner.
     * 
     * @param context
     * @param folder
     * @param document
     * @param content
     * @return 
     * @throws com.ozguryazilim.telve.attachment.AttachmentException 
     */
    AttachmentDocument addDocument( AttachmentContext context, AttachmentFolder folder, AttachmentDocument document, byte[] content) throws AttachmentException;
    
    //TODO: update ve delete methodları
    
    /**
     * Verilen ID'li documanı siler
     * @param context
     * @param id
     * @throws AttachmentException 
     */
    void deleteDocument( AttachmentContext context, String id ) throws AttachmentException;
    
    /**
     * Verilen parent folder'a yeni bir folder ekler.
     * @param context
     * @param parent
     * @param name
     * @return 
     * @throws com.ozguryazilim.telve.attachment.AttachmentException 
     */
    AttachmentFolder addFolder( AttachmentContext context, AttachmentFolder parent, String name ) throws AttachmentException;

    /**
     * Verilen path'e folder açar
     * @param context
     * @param path
     * @return 
     * @throws com.ozguryazilim.telve.attachment.AttachmentException 
     */
    AttachmentFolder addFolder(AttachmentContext context, String path) throws AttachmentException;

    void deleteFolder( AttachmentContext context, String path ) throws AttachmentException;

    
}
