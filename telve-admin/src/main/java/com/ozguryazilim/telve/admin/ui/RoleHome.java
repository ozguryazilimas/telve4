/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.ui;

import com.ozguryazilim.telve.admin.AbstractIdentityHome;
import com.ozguryazilim.telve.permisson.PermissionDefinition;
import com.ozguryazilim.telve.permisson.PermissionGroup;
import com.ozguryazilim.telve.permisson.PermissionRegistery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.permission.IdentityPermission;
import org.picketlink.idm.permission.Permission;

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

    @Inject
    private PartitionManager partitionManager;
    @Inject
    private PermissionManager permissionManager;

    private Map<String, PermissionGroupUIModel> permissionGroups = new HashMap<>();

    @Override
    public List<Role> getEntityList() {
        return getIdentityManager().createIdentityQuery(Role.class).getResultList();
    }

    @Override
    public void createNew() {
        setCurrent(new Role());
    }

    @Override
    public void setCurrent(Role current) {
        super.setCurrent(current); 
        buildPermissionModel();
        buildPermissionModelValues();
    }

    
    public List<PermissionGroupUIModel> getPermissions() {
        return new ArrayList(permissionGroups.values());
    }

    protected void buildPermissionModel() {
        permissionGroups.clear();
        for (PermissionGroup pg : PermissionRegistery.instance().getPermMap().values()) {
            for (PermissionDefinition pd : pg.getDefinitions()) {
                addPermissionModel(pg.getName(), pd );
            }
        }
    }

    protected void buildPermissionModelValues() {
        for (PermissionGroupUIModel pg : permissionGroups.values()) {
            for (PermissionUIModel pd : pg.getPermissions()) {
                for (Permission perm : permissionManager.listPermissions(pd.getDefinition().getTarget())) {
                    if (perm instanceof IdentityPermission) {
                        IdentityPermission ip = (IdentityPermission) perm;
                        if (ip.getAssignee().equals(getCurrent())) {
                            pd.grantPermission(ip.getOperation());
                        }
                    }
                }
            }
        }
    }

    private void addPermissionModel(String group, PermissionDefinition pd) {
        PermissionGroupUIModel pgm = permissionGroups.get(group);
        if (pgm == null) {
            pgm = new PermissionGroupUIModel();
            pgm.setName(group);
            permissionGroups.put(group, pgm);
        }

        PermissionUIModel pm = new PermissionUIModel();
        pm.setDefinition(pd);
        pgm.getPermissions().add(pm);
    }

}
