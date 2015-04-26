/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.entities.StoredCommand_;
import com.ozguryazilim.telve.forms.Browse;
import com.ozguryazilim.telve.forms.BrowseBase;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.DateColumn;
import com.ozguryazilim.telve.query.columns.TextColumn;
import com.ozguryazilim.telve.view.Pages;
import java.util.List;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * saklanmış komutlar için tarama view control sınıfı.
 * 
 * FIXME: Silerken zamanlanmış bütün görevlerde silinmeli.
 * 
 * @author Hakan Uygun
 */
@Browse(browsePage = Pages.Admin.StoredCommandBrowse.class, editPage = DefaultErrorView.class, viewContainerPage = DefaultErrorView.class )
public class StoredCommandBrowse extends BrowseBase<StoredCommand, StoredCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(StoredCommandBrowse.class);
    
    private StoredCommand entity;
    private CommandEditorBase currentEditor;
    
    @Inject
    private StoredCommandRepository repository;
    
    @Inject
    private CommandSender commandSender;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<StoredCommand, StoredCommand> queryDefinition) {
        //queryDefinition
        //        .addFilter(new Tex<>(SuggestionItem_.group, SuggestionGroupRegistery.intance().getGroupNames(), "general.label.Group", "suggestionGroup.label."), true, true)
        //        .addFilter(new StringFilter<>(SuggestionItem_.key, "general.label.Key"), true );
        
        queryDefinition.addColumn(new TextColumn<>(StoredCommand_.name, "general.label.Name"),true);
        queryDefinition.addColumn(new TextColumn<>(StoredCommand_.info, "general.label.Info"),true);
        queryDefinition.addColumn(new DateColumn<>(StoredCommand_.createDate, "general.label.CreateDate"),true);
        queryDefinition.addColumn(new TextColumn<>(StoredCommand_.createBy, "general.label.CreateBy"),true);
        queryDefinition.addColumn(new TextColumn<>(StoredCommand_.schedule, "general.label.Schedule"),true);
    }

    @Override
    protected RepositoryBase<StoredCommand, StoredCommand> getRepository() {
        return repository;
    }
    
    
    public List<String> getEditorNames(){
        return CommandEditorRegistery.getEditorNames();
    }

    /**
     * Adı verilen editor ile yeni bir kayıt oluşturur.
     * @param name 
     */
    public void createNew( String name ){
     
        CommandEditorBase ce = CommandEditorRegistery.getEditorByName(name);
        if( ce == null ){
            LOG.error("CommandEditor not found : {}", name);
            FacesMessages.error( "general.message.editor.NotFound");
            return;
        }
        ce.createNew();
        currentEditor = ce;
    }
    
    /**
     * Geriye editör/komut için tanıtılmış icon varsa onu döndürür.
     * 
     * Bulamazsa standart icon döndürür.
     * 
     * @return 
     */
    public String getIconPath( String name ){
        //FIXME: Doğru düzgün registery'den alınmalı.
        return "/img/ribbon/large/report.png";
    }

    /**
     * Aktif olan editor'ü döndürür.
     * @return 
     */
    public CommandEditorBase getCurrentEditor() {
        return currentEditor;
    }
    
    public void edit(){
        entity = selectedItem;
        CommandEditorBase ce = CommandEditorRegistery.getEditorByCommand(entity.getType());
        if( ce == null ){
            LOG.error("CommandEditor not found for : {}", entity.getType());
            FacesMessages.error("general.message.editor.NotFound");
            return;
        }
        currentEditor = ce;
        try {
            ce.edit(entity);
        } catch (ClassNotFoundException ex) {
            //FIXME: Burası için aslında generik hata sayfasına gidilebilir...
            LOG.error("Command Class not found: {}", entity.getType(), ex);
            FacesMessages.error("general.message.editor.NotFound");
        }
    }
    
    @Transactional
    public void delete(){
        entity = selectedItem;
        repository.remove(entity);
    }
    
    public void schedule(){
        //FIXME: Burada kullanıcıya Sheduler Editorü açılacak
    }
    
    public void run(){
        try {
            commandSender.sendCommand(repository.convert(selectedItem));
            //FIXME: Burada kullanıcıya gerçekten çalıştırmak isteyip istemediği sorulacak.
        } catch (ClassNotFoundException ex) {
            //FIXME: Burada ne yapmalı?!
        }
    }
    
}
