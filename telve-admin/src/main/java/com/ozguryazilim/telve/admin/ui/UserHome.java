/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.admin.ui;

import com.ozguryazilim.telve.admin.AbstractIdentityHome;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.primefaces.model.DualListModel;

/**
 * Kullanıcı tanımlama Ekranı.
 * PicketLink IDM üzerinden çalışır. 
 * 
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class UserHome extends AbstractIdentityHome<User>{
    
    
    private DualListModel<Role> roleList;
    
    @Override
    public List<User> getEntityList(){
        return getIdentityManager().createIdentityQuery(User.class).getResultList();
    }

    @Override
    public void createNew(){
        setCurrent(new User());
    }
    
    public DualListModel<Role> getRoleList() {
        if( roleList == null ){
            initRoleList();
        }
        return roleList;
    }

    public void setRoleList(DualListModel<Role> roleList) {
        this.roleList = roleList;
    }

    private void initRoleList() {
        List<Role> target = new ArrayList<>();
        List<Role> source = getIdentityManager().createIdentityQuery(Role.class).getResultList();
        
        roleList = new DualListModel<>(source, target);
    }

    public List<Role> getRoles() {
        return getIdentityManager().createIdentityQuery(Role.class).getResultList();
    }

    @Override
    public void save() {
        super.save(); 
    }
    
}
