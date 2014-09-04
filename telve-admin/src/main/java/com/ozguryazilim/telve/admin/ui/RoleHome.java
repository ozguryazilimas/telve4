/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.admin.ui;

import com.ozguryazilim.telve.admin.AbstractIdentityHome;
import java.util.List;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.picketlink.idm.model.basic.Role;

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
public class RoleHome extends AbstractIdentityHome<Role>{
    
    @Override
    public List<Role> getEntityList(){
        return getIdentityManager().createIdentityQuery(Role.class).getResultList();
    }

    
    @Override
    public void createNew(){
        setCurrent(new Role());
    }
    
}
