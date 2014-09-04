/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.admin;

import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.IdentityType;

/**
 * PicketLink Identity Nesneleri UI Kontrol sınıfları için Taban.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractIdentityHome<E extends IdentityType> implements Serializable{
    
    private E current;
    private List<E> filteredList;
    
    @Inject
    private GroupedConversation conversation;
    
    @Inject
    private IdentityManager identityManager;
    
    public abstract List<E> getEntityList();

    public E getCurrent() {
        if( current == null ){
            createNew();
        }
        return current;
    }

    public void setCurrent(E current) {
        this.current = current;
    }
    
    public abstract void createNew();
    
    @Transactional
    public void save(){
        identityManager.add(current);
    }
    
    @Transactional
    public void saveAndNew(){
        save();
        createNew();
    }
    
    @Transactional
    public void delete(){
        //identityManager.
    }
    
    public Class<? extends ViewConfig> close(){
        conversation.close();
        return Pages.Home.class;
    }
    
    public void edit( E r ){
        current = r;
    }

    public List<E> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<E> filteredList) {
        this.filteredList = filteredList;
    }

    public IdentityManager getIdentityManager() {
        return identityManager;
    }
    
    
}
