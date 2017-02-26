/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.note;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.FeaturePointer;
import com.ozguryazilim.telve.entities.Note;
import com.ozguryazilim.telve.forms.EntityChangeAction;
import com.ozguryazilim.telve.forms.EntityChangeEvent;
import com.ozguryazilim.telve.qualifiers.AfterLiteral;
import com.ozguryazilim.telve.qualifiers.EntityQualifierLiteral;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 * Note Widget Control Sınıfı.
 * 
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class NoteWidget implements Serializable{
    
    @Inject
    private Identity identity;
    
    @Inject
    private NoteRepository repository;
    
    @Inject
    private Event<EntityChangeEvent> entityChangeEvent;
    
    /**
     * Widget'in hangi Feature için çalıştığının göstergesi
     */
    private FeaturePointer featurePointer;
    private Boolean editMode = Boolean.FALSE;
    private Note note;
    
    public void init( FeaturePointer featurePointer ){
        this.featurePointer = featurePointer;
    }
    
    public void addNewNote(){
        note = new Note();
        note.setFeaturePointer(featurePointer);
        note.setOwner(identity.getLoginName());
        note.setCreateDate(new Date());
        note.setPriority("NORMAL");
        
        editMode = Boolean.TRUE;
    }

    public void save(){
        repository.save(note);
        
        entityChangeEvent
                    .select(new EntityQualifierLiteral(Note.class))
                    .select(new AfterLiteral())
                    .fire(new EntityChangeEvent(note, EntityChangeAction.INSERT ));

        
        editMode = Boolean.FALSE;
    }
    
    @Transactional
    public void delete(Note note){
        repository.remove(note);
    }
    
    public void togglePriority(){
        if( "NORMAL".equals(note.getPriority())){
            note.setPriority("IMPORTANT");
        } else {
            note.setPriority("NORMAL");
        }
    }
    
    public void cancel(){
        note = null;
        editMode = Boolean.FALSE;
    }
    
    public List<Note> getNotes(){
        return repository.findByFeature(featurePointer);
    }
    
    public Boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(Boolean editMode) {
        this.editMode = editMode;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    
}
