/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.role;

import com.google.common.base.Splitter;
import com.ozguryazilim.telve.admin.ui.PermissionValueModel;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.ChangeLogStore;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.ParamBase;
import com.ozguryazilim.telve.forms.ParamEdit;
import com.ozguryazilim.telve.idm.IdmEvent;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.RolePermission;
import com.ozguryazilim.telve.permisson.PermissionAction;
import com.ozguryazilim.telve.permisson.PermissionDefinition;
import com.ozguryazilim.telve.permisson.PermissionGroup;
import com.ozguryazilim.telve.permisson.PermissionRegistery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rol tanımlama control sınıfı
 * 
 * @author Hakan Uygun
 */
@ParamEdit
public class RoleHome extends ParamBase<Role, Long>{

    private static final Logger LOG = LoggerFactory.getLogger(RoleHome.class);
    
    @Inject
    private RoleRepository repository;
    
    @Inject
    private Identity identity;

    @Inject 
    private Event<IdmEvent>  event;
    
    private Map<String, PermissionGroup> permissonMap = new HashMap<>();
    
    private ChangeLogStore changeLogStore = new ChangeLogStore();
    
    private PermissionValueModel permissionValueModel = new PermissionValueModel();
    
    @PostConstruct
    public void init() {
        permissonMap = PermissionRegistery.instance().getPermMap();
        
    }
    
    protected void buildPermissionModelValues() {
            
        permissionValueModel.clear();
        changeLogStore.clear();
        
        for( RolePermission rp : getEntity().getPermissions() ){
            //FIXME: Bu kod buradan bir util sınıfa taşınmalı 
            //FIXME: şu anda sadece domain:action,action,action yapısı destekleniyor.
            
            //TODO:changeLogStore konusunu bir düşünelim.
            List<String> parts = Splitter.on(':').trimResults().omitEmptyStrings().splitToList(rp.getPermission());
            List<String> actions = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(parts.get(1));
            changeLogStore.addOldValue("permission.label." + parts.get(0), parts.get(1));
            
            permissionValueModel.setPermissionValues(rp.getPermission());
            
        }
        
    }

    @Override
    public boolean onBeforeSave() {
        //Role için code ve name aslında aynı değer. ParamBase'i kullanmak için hile yapıyoruz.
        getEntity().setName(getEntity().getCode());
        
        //FIXME: Aşağıdaki kodun biraz daha adam edilip başka yere taşınması lazım
        //Var olan yetkileri silelim
        getEntity().getPermissions().clear();
        
        //Şimdi de yeni durumu ekleyelim
        for (String prs : permissionValueModel.getPermissionValues()) {
            
                RolePermission rp = new RolePermission();
                rp.setRole(getEntity());
                rp.setPermission( prs );
                getEntity().getPermissions().add(rp);
                //FIXME: Buna bir çözüm bulunacak
                //changeLogStore.addNewValue("permission.label." + domain, acts);
        }
        
        
        return super.onBeforeSave();
    }

    @Override
    public boolean onAfterLoad() {
        buildPermissionModelValues();
        return super.onAfterLoad(); 
    }

    @Override
    public void createNew() {
        super.createNew(); 
        buildPermissionModelValues();
    }
    
    

    @Override
    public boolean onAfterSave() {
        event.fire(new IdmEvent(IdmEvent.FROM_ROLE, IdmEvent.CREATE, getEntity().getName()));
        return super.onAfterSave();
    }

    @Override
    public boolean onBeforeDelete() {
        event.fire(new IdmEvent(IdmEvent.FROM_ROLE, IdmEvent.DELETE, getEntity().getName()));
        return super.onAfterDelete();
    }
    
    @Override
    protected RepositoryBase<Role, ?> getRepository() {
        return  repository;
    }

    @Override
    protected void auditLog(String action) {
        getAuditLogger().actionLog(getEntity().getClass().getSimpleName(), getEntity().getId(), getBizKeyValue(), AuditLogCommand.CAT_AUTH,  action, identity.getLoginName(), "", changeLogStore.getChangeValues());
    }
    
    
    public List<PermissionGroup> getPermissionGroups(){
        return PermissionRegistery.instance().getPermissionGroups();
    }

    public PermissionValueModel getPermissionValueModel() {
        return permissionValueModel;
    }
    
    /**
     * Verilen domain:action için scope değerini sıradakine setler
     * 
     * @param domain
     * @param action 
     */
    public void setNextValue( String domain, String action ){
       
        PermissionDefinition pd = PermissionRegistery.instance().getPermissionMap().get(domain);
        if( pd != null ){
            
            String curVal = permissionValueModel.getValue(domain, action);
            String nextVal = "NONE";
            
            for( PermissionAction a : pd.getActions()){
                if( a.getName().equals(action)){
                    int ix = a.getScopes().indexOf(curVal);
                    if( a.getScopes().size() <= ix + 1){
                        nextVal = a.getScopes().get(0);
                    } else {
                        nextVal = a.getScopes().get(ix + 1);
                    }
                }
            }
            
            permissionValueModel.setValue(domain, action, nextVal);
        }
        
    }
    
    
    public void setNextValue( String domain ){
        PermissionDefinition pd = PermissionRegistery.instance().getPermissionMap().get(domain);
        if( pd != null ){
         
            for( PermissionAction a : pd.getActions()){
                
                String curVal = permissionValueModel.getValue(domain, a.getName());
                String nextVal = "NONE";
            
                int ix = a.getScopes().indexOf(curVal);
                if( a.getScopes().size() <= ix + 1){
                    nextVal = a.getScopes().get(0);
                } else {
                    nextVal = a.getScopes().get(ix + 1);
                }
                
                permissionValueModel.setValue(domain, a.getName(), nextVal);
            }
            
            
        }
    }
    
    /**
     * Bir grup içinde bütün permissionların verilen action'nı bir sonraki değere taşır
     * @param group
     * @param action 
     */
    public void setNextActionValue( String group, String action ){
        PermissionGroup pg = PermissionRegistery.instance().getPermMap().get(group);
        for( PermissionDefinition pd : pg.getDefinitions()){
            setNextValue( pd.getName(), action );
        }
    }
}
