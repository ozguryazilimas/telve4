/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.note;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.entities.Note;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.picketlink.Identity;
import org.primefaces.context.RequestContext;

/**
 * Note işlemleri için temel View Control sınıfı.
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class NoteController implements Serializable{
    
    @Inject
    private NoteRepository repository;
    
    @Inject
    private Identity identity;
 
    @Inject
    private FacesContext facesContext;
    
    private Note note;
    
    private List<Note> notes;
    
    public void createNewNote(){
        note = new Note();
        note.setCreateDate(new Date());
        note.setOwner(identity.getAccount().getId());
        note.setPermission("OWNER");
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    
    public void openNewNoteDialog( String attachment){
        createNewNote();
        note.setAttachtment( calcAttachmentURI(attachment) );
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);
        
        RequestContext.getCurrentInstance().openDialog("/note/newNotePopup", options, null);
    }
    
    public void openNewNoteDialog(){
        createNewNote();
        note.setAttachtment("GLOBAL");
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);
        
        RequestContext.getCurrentInstance().openDialog("/note/newNotePopup", options, null);
    }
    
    public void closeDialog() {
        save();
        RequestContext.getCurrentInstance().closeDialog("Note Saklandı");
    }

    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog("Nottan İptalle Çıkıldı");
    }
    
    
    public void openNotesDialog( String attachment ){
        
        System.out.println(attachment);
        
       //TODO: hasNotes methodu değiştiğinde note yükleme kısmı buraya alınmalı. 
        notes = repository.findNotes(identity.getAccount().getId(), calcAttachmentURI(attachment));
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);
        
        RequestContext.getCurrentInstance().openDialog("/note/notesPopup", options, null);
    }
    
    /**
     * İlgili sayfa için note var mı kontrolü.
     * 
     * Aynı zamanda sayfa ile ilgili notları da yükler.
     * 
     * TODO: Peformans için sadece count almalı. Ve mümkünse chachlenmeli...
     * 
     * @param attachment
     * @return 
     */
    public Boolean hasNotes( String attachment ){
        System.out.println(attachment);
        notes = repository.findNotes(identity.getAccount().getId(), calcAttachmentURI());
        return !notes.isEmpty();
    } 
    
    
    public Boolean canAddNote(){
        
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        
        String uri = request.getRequestURI();
        
        //Eğer eid=0 varsa yeni giriş ekranıdır ve note ekletmiyoruz.
        String eid = request.getParameter("eid");
        if( !Strings.isNullOrEmpty(eid) && "0".equals(eid)){
            return Boolean.FALSE;  
        }
        
        return Boolean.TRUE;
    }
    
    /**
     * Üzerinde çalışma yapılan notu saklar.
     */
    @Transactional
    public void save(){
        repository.save(note);
    }
    
    
    /**
     * Gelen string içinden eid alınıp requestURI ile birleştiriliyor.
     * @param url
     * @return 
     */
    protected String calcAttachmentURI( String url ){
        
        // http://localhost:8080/openohsce/controlList/controlListView.jsf?eid=1401&dswid=8674
        //Şekilnde gelen stringi önce ? işareti ile ayırdık.
        List<String> ls = Splitter.on('?').splitToList(url);
        //Şimdide map'a aldık.
        Map<String,String> m = Splitter.on('&').omitEmptyStrings().trimResults().withKeyValueSeparator('=').split(ls.get(1));
        
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        
        String uri = request.getRequestURI();
        
        if( m.get("eid") != null ){
            uri = uri + "?eid=" + m.get("eid");
        }
        
        return uri;
    }
    
    /**
     * Doğrudan requestContext üzerinden hesaplama yapar.
     * @return 
     */
    protected String calcAttachmentURI(){
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        
        String uri = request.getRequestURI();
        
        if( request.getParameter("eid") != null ){
            uri = uri + "?eid=" + request.getParameter("eid");
        }
        
        return uri;
    }
    
}
