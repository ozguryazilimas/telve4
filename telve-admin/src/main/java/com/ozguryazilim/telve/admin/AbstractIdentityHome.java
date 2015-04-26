/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.admin;

import com.ozguryazilim.telve.auth.AbstractIdentityHomeExtender;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
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
    
    public abstract List<AbstractIdentityHomeExtender> getExtenders();

    public E getCurrent() {
        if( current == null ){
            doCreateNew();
        }
        return current;
    }

    public void setCurrent(E current) {
        this.current = current;
    }
    
    /**
     * Yeni bir identity nesnesi oluşturulup current içine setlenmelidir.
     */
    public abstract void createNew();
    
    
    /**
     * Yeni bir identity nesnesi oluşturulması için wrapper fonksiyon
     * @return 
     */
    protected boolean doCreateNew(){
        if( !doBeforeNew() ){
            return false;
        }
        
        createNew();
        
        return doAfterNew();
    }
    
    /**
     * Mevcut identity nesnesini saklar.
     * @return 
     */
    @org.apache.deltaspike.jpa.api.transaction.Transactional
    public boolean save(){
        if( !doBeforeSave() ){
            return false;
        }
        if( current.getId() != null ){
            identityManager.update(current);
        } else {
            identityManager.add(current);
        }
        
        FacesMessages.info( "general.message.record.SaveSuccess");
        
        return doAfterSave();
    }
    
    /**
     * Mevcut identity nesnesini saklar ve yeni bir tane hazırlar.
     */
    @org.apache.deltaspike.jpa.api.transaction.Transactional
    public void saveAndNew(){
        if( save() ){
            doCreateNew();
        }
    }
    
    /**
     * Mevcut identity nesnesini siler.
     */
    @org.apache.deltaspike.jpa.api.transaction.Transactional
    public void delete(){
        //FIXME: Silme işlemi yapılmalı...
        //identityManager.
        FacesMessages.info("general.message.record.DeleteSuccess");
    }
    
    /**
     * Formu ve scope'u kapatır.
     * @return 
     */
    public Class<? extends ViewConfig> close(){
        conversation.close();
        return Pages.Home.class;
    }
    
    /**
     * Listeden gelen bileşeni current olarak atar. 
     * Böylece edit mode'a geçirir.
     * @param r 
     */
    public void edit( E r ){
        doBeforeLoad();
        current = r;
        doAfterLoad();
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
    
    /**
     * Save işleminden hemen önce extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doBeforeSave(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onBeforeSave( current );
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Save işleminden hemen sonra extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doAfterSave(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onAfterSave( current );
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Yeni identity oluşturulmadan hemen önce extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doBeforeNew(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onBeforeNew( current );
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Yeni identity oluşturulduktan hemen sonra extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doAfterNew(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onAfterNew( current );
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    
    /**
     * Silme işleminden hemen önce extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doBeforeDelete(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onBeforeDelete( current);
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Silme işleminden hemen sonra extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doAfterDelete(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onAfterDelete( current);
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Yükleme işleminden hemen önce extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doBeforeLoad(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onBeforeLoad( current);
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Yükleme işleminden hemen sonra extenderlar çağırılır. 
     * 
     * eğer olumsuz bir şey varsa false döner.
     * 
     * @return 
     */
    protected boolean doAfterLoad(){
        boolean result = true;
        for( AbstractIdentityHomeExtender e : getExtenders() ){
            result = e.onAfterLoad( current);
            if( !result ){
                return result;
            }
        }
        
        return result;
    }
}
