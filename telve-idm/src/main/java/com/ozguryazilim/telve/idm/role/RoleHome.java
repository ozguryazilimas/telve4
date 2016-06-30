/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.role;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.admin.ui.PermissionGroupUIModel;
import com.ozguryazilim.telve.admin.ui.PermissionUIModel;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.ParamBase;
import com.ozguryazilim.telve.forms.ParamEdit;
import com.ozguryazilim.telve.idm.IdmEvent;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.RolePermission;
import com.ozguryazilim.telve.permisson.ActionConsts;
import com.ozguryazilim.telve.permisson.PermissionDefinition;
import com.ozguryazilim.telve.permisson.PermissionGroup;
import com.ozguryazilim.telve.permisson.PermissionRegistery;
import java.util.ArrayList;
import java.util.Comparator;
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
    private Event<IdmEvent>  event;
    
    private Map<String, PermissionGroupUIModel> permissionGroups = new HashMap<>();
    private Map<String, PermissionUIModel> permissionModels = new HashMap<>();

    
    @PostConstruct
    public void init() {
        buildPermissionModel();
    }
    
    protected void buildPermissionModel() {
        permissionGroups.clear();
        for (PermissionGroup pg : PermissionRegistery.instance().getPermMap().values()) {
            for (PermissionDefinition pd : pg.getDefinitions()) {
                addPermissionModel(pg, pd);
            }
        }
    }

    protected void buildPermissionModelValues() {
        clearPermissionValues();
            
        for( RolePermission rp : getEntity().getPermissions() ){
            //FIXME: Bu kod buradan bir util sınıfa taşınmalı 
            //FIXME: şu anda sadece domain:action,action,action yapısı destekleniyor.
            List<String> parts = Splitter.on(':').trimResults().omitEmptyStrings().splitToList(rp.getPermission());
            List<String> actions = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(parts.get(1));
            for( String action : actions ){
                setPermissionValue(parts.get(0), action);
            }
        }
        
        /*
        changeLogStore.clear();
        
        for (Permission perm : getCurrentPermmissions()) {
            setPermissionValue((String)perm.getResourceIdentifier(), perm.getOperation());
            changeLogStore.appendOldValue("permission.label." + (String)perm.getResourceIdentifier(), perm.getOperation());
        }
        */
    }

    private void addPermissionModel(PermissionGroup group, PermissionDefinition pd) {
        PermissionGroupUIModel pgm = permissionGroups.get(group.getName());
        if (pgm == null) {
            pgm = new PermissionGroupUIModel();
            pgm.setName(group.getName());
            pgm.setOrder(group.getOrder());
            permissionGroups.put(group.getName(), pgm);
        }

        PermissionUIModel pm = new PermissionUIModel();
        pm.setDefinition(pd);
        pgm.getPermissions().add(pm);

        //Ayrıca modellerin indeksi
        permissionModels.put(pm.getDefinition().getTarget(), pm);
    }

    /**
     * Daha önce atanmış bütün değerleri siler.
     */
    protected void clearPermissionValues() {
        for (PermissionUIModel pd : permissionModels.values()) {
            pd.clearValues();
        }
    }

    /**
     * Verilen target'a verilen değeri atar
     *
     * @param target
     * @param action
     */
    protected void setPermissionValue(String target, String action) {
        PermissionUIModel pd = permissionModels.get(target);
        if (pd != null) {
            pd.grantPermission(action);
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
        for (PermissionUIModel pd : permissionModels.values()) {
            String domain = pd.getDefinition().getTarget();
            List<String> actions = new ArrayList<>();
            
            if( pd.hasPermission( ActionConsts.SELECT_ACTION )){
                actions.add(ActionConsts.SELECT_ACTION);
            }
            
            if( pd.hasPermission( ActionConsts.INSERT_ACTION )){
                actions.add(ActionConsts.INSERT_ACTION);
            }
            
            if( pd.hasPermission( ActionConsts.UPDATE_ACTION )){
                actions.add(ActionConsts.UPDATE_ACTION);
            }
            
            if( pd.hasPermission( ActionConsts.DELETE_ACTION )){
                actions.add(ActionConsts.DELETE_ACTION);
            }
            
            if( pd.hasPermission( ActionConsts.EXEC_ACTION )){
                actions.add(ActionConsts.EXEC_ACTION);
            }
            
            if( pd.hasPermission( ActionConsts.EXPORT_ACTION )){
                actions.add(ActionConsts.EXPORT_ACTION);
            }
            
            String acts = Joiner.on(',').join(actions);
            
            if( !Strings.isNullOrEmpty(acts)){
                RolePermission rp = new RolePermission();
                rp.setRole(getEntity());
                rp.setPermission(domain + ":" + acts );
                getEntity().getPermissions().add(rp);
            }
        }
        
        
        return super.onBeforeSave();
    }

    @Override
    public boolean onAfterLoad() {
        buildPermissionModelValues();
        return super.onAfterLoad(); 
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
    
    public List<PermissionGroupUIModel> getPermissions() {
        List<PermissionGroupUIModel> ls = new ArrayList(permissionGroups.values());
        ls.sort(new Comparator<PermissionGroupUIModel>() {
            @Override
            public int compare(PermissionGroupUIModel t, PermissionGroupUIModel t1) {
                int result = t.getOrder().compareTo(t1.getOrder());
                if( result == 0 ){
                    result = t.getName().compareTo(t1.getName());
                }
                return result;
            }
        });
        return ls;
    }
    
    
    @Override
    protected RepositoryBase<Role, ?> getRepository() {
        return  repository;
    }
    
}
