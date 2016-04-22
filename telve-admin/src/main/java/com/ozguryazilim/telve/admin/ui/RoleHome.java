/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.ui;

import com.ozguryazilim.telve.admin.AbstractIdentityHome;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.audit.ChangeLogStore;
import com.ozguryazilim.telve.auth.AbstractIdentityHomeExtender;
import com.ozguryazilim.telve.auth.ActiveUserLookup;
import com.ozguryazilim.telve.entities.AuditLogDetail;
import com.ozguryazilim.telve.permisson.ActionConsts;
import com.ozguryazilim.telve.permisson.PermissionDefinition;
import com.ozguryazilim.telve.permisson.PermissionGroup;
import com.ozguryazilim.telve.permisson.PermissionRegistery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.permission.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Role Tanımları Home Sınıfı.
 *
 * PicketLink IDM üzerinden çalışır.
 *
 * JPA Store Kullanır
 *
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class RoleHome extends AbstractIdentityHome<Role> {

    private static final Logger LOG = LoggerFactory.getLogger(RoleHome.class);

    @Inject
    private PartitionManager partitionManager;
    @Inject
    private PermissionManager permissionManager;

    @Inject
    private AuditLogger auditLogger;
    
    @Inject 
    private ActiveUserLookup userLookup;
    
    private Map<String, PermissionGroupUIModel> permissionGroups = new HashMap<>();
    private Map<String, PermissionUIModel> permissionModels = new HashMap<>();
    private List<Permission> currentPermmissions;

    private ChangeLogStore changeLogStore = new ChangeLogStore();
    
    @Override
    public List<Role> getEntityList() {
        return getIdentityManager().createIdentityQuery(Role.class)
                .sortBy(getIdentityManager().getQueryBuilder().asc(Role.NAME))
                .getResultList();
    }

    @PostConstruct
    public void init() {
        buildPermissionModel();
    }

    @Override
    public void createNew() {
        setCurrent(new Role());
        doAfterNew();
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
                
        changeLogStore.clear();
        
        for (Permission perm : getCurrentPermmissions()) {
            setPermissionValue((String)perm.getResourceIdentifier(), perm.getOperation());
            changeLogStore.appendOldValue("permission.label." + (String)perm.getResourceIdentifier(), perm.getOperation());
        }
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
    public List<AbstractIdentityHomeExtender> getExtenders() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected boolean doAfterLoad() {
        currentPermmissions = null;
        buildPermissionModelValues();
        LOG.info("Current Perms : {}", currentPermmissions);
        return super.doAfterLoad();
    }

    @Override
    protected boolean doAfterNew() {
        currentPermmissions = null;
        buildPermissionModelValues();
        return super.doAfterNew();
    }

    public List<Permission> getCurrentPermmissions() {
        if (currentPermmissions == null) {
            if( getCurrent().getId() == null ){
                currentPermmissions = Collections.EMPTY_LIST;
            } else {
                currentPermmissions = permissionManager.listPermissions(getCurrent());
            }
        }
        return currentPermmissions;
    }

    protected boolean hasCurrentPermission(String target, String action) {

        for (Permission perm : currentPermmissions) {
            if (target.equals(perm.getResourceIdentifier()) && action.equals(perm.getOperation())) {
                return true;
            }
        }

        return false;
    }

    protected void grantRevokePermission(PermissionUIModel pd, String action) {
        if (pd.hasPermission(action)) {
            permissionManager.grantPermission(getCurrent(), pd.getDefinition().getTarget(), action);
            changeLogStore.appendNewValue("permission.label." + pd.getDefinition().getTarget(), action);
        } else if (hasCurrentPermission(pd.getDefinition().getTarget(), action)) {
            permissionManager.revokePermission(getCurrent(), pd.getDefinition().getTarget(), action);
        }
    }

    @Override
    protected boolean doAfterSave() {

        for (PermissionUIModel pd : permissionModels.values()) {
            grantRevokePermission( pd, ActionConsts.SELECT_ACTION );
            grantRevokePermission( pd, ActionConsts.INSERT_ACTION );
            grantRevokePermission( pd, ActionConsts.UPDATE_ACTION );
            grantRevokePermission( pd, ActionConsts.DELETE_ACTION );
            grantRevokePermission( pd, ActionConsts.EXPORT_ACTION );
            grantRevokePermission( pd, ActionConsts.EXEC_ACTION );
        }

        List<AuditLogDetail> changes = changeLogStore.getChangeValues();
        auditLogger.authLog( getCurrent().getClass().getSimpleName(), getCurrent().getName(), userLookup.getActiveUser().getLoginName(), getCurrent().getName() + " rol yetkileri değişti", changes );
        
        return super.doAfterSave();
    }

    @Override
    protected boolean doBeforeDelete() {
        
        auditLogger.actionLog( getCurrent().getClass().getSimpleName(), null, getCurrent().getName(),  AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_DELETE, userLookup.getActiveUser().getLoginName(), getCurrent().getName() + " rol silindi");
        
        return super.doAfterDelete(); 
    }

    
}
