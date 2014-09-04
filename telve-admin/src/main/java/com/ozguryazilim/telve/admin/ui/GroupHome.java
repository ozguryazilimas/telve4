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
import org.picketlink.idm.model.basic.Group;

/**
 * User Group UI kontrol sınıfı.
 * 
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class GroupHome extends AbstractIdentityHome<Group>{

    @Override
    public void createNew() {
        setCurrent(new Group());
    }

    @Override
    public List<Group> getEntityList() {
        return getIdentityManager().createIdentityQuery(Group.class).getResultList();
    }
    
}
