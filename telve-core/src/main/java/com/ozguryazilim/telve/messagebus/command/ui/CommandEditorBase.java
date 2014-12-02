/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import java.io.Serializable;
import java.util.Date;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.util.ProxyUtils;
import org.apache.deltaspike.jpa.api.transaction.Transactional;


/**
 * Komut editörleri için taban sınıf.
 * 
 * Temel olarak UI Controller sınıfı. Tek bir komut ile ilgili yapılacak olan işlemleri tutar.
 * 
 * @author Hakan Uygun
 * @param <C> Editörün işleyeceği komut sınıfı
 */
public abstract class CommandEditorBase< C extends StorableCommand> implements Serializable{
    
    private C command;
    private StoredCommand entity;
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private StoredCommandRepository repository;
    
    public void createNew(){
        entity = new StoredCommand();
        command = createNewCommand();
        entity.setName(command.getName());
        entity.setCreateBy("");
        entity.setCreateDate(new Date());
    }

    public abstract C createNewCommand();


    @Transactional
    public void save(){
        onBeforeSave();
        
        
        
        entity = repository.merge(entity, command);
        repository.save(entity);
        
        onAfterSave();
    }
    
    
    public void edit( StoredCommand entity ) throws ClassNotFoundException{
        
        onBeforeLoad();
        
        this.entity = entity;
        this.command = (C) repository.convert(entity);
        
        onAfterLoad();
    }
    
    public void init( StorableCommand command ){
        this.command = (C)command;
    }

    /**
     * Yükleme işleminden hemen önce çağrılır.
     */
    public void onBeforeLoad(){}
    
    /**
     * Yükleme işleminden hemen donra çağrılır.
     */
    public void onAfterLoad(){}
    
    /**
     * Saklama işleminden hemen önce çağrılır.
     */
    public void onBeforeSave(){}
    
    /**
     * Saklama işleminden hemen donra çağrılır.
     */
    public void onAfterSave(){}
    
    /**
     * Silme işleminden hemen önce çağrılır.
     */
    public void onBeforeDelete(){}
    
    /**
     * Silme işleminden hemen donra çağrılır.
     */
    public void onAfterDelete(){}

    public C getCommand() {
        return command;
    }

    public void setCommand(C command) {
        this.command = command;
    }

    public StoredCommand getEntity() {
        return entity;
    }

    public void setEntity(StoredCommand entity) {
        this.entity = entity;
    }
    

    /**
     * Dialog için sınıf annotationı üzerinden aldığı Page ID'sini döndürür.
     *
     * @return
     */
    public String getDialogPageViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getDialogPage()).getViewId();
    }
    
    /**
     * Sınıf işaretçisinden @CommandEditor page bilgisini alır
     *
     * @return
     */
    public Class<? extends ViewConfig> getDialogPage() {
        CommandEditor a = (CommandEditor)ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(CommandEditor.class);
        return a.page();
    }
}
