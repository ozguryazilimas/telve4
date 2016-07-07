/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.role;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.Role_;
import com.ozguryazilim.telve.lookup.Lookup;
import com.ozguryazilim.telve.lookup.LookupTableControllerBase;
import com.ozguryazilim.telve.lookup.LookupTableModel;
import javax.inject.Inject;

/**
 * Role Lookup
 * 
 * FIXME: Role tipine göre profil uygulamamız gerek
 * 
 * @author Hakan Uygun
 */
@Lookup(dialogPage = IdmPages.RoleLookup.class)
public class RoleLookup extends LookupTableControllerBase<Role, Role>{

    @Inject
    private RoleRepository repository;
    
    @Override
    protected void buildModel(LookupTableModel<Role> model) {
        model.addColumn("name", "general.label.Name");
    }

    @Override
    protected RepositoryBase<Role, Role> getRepository() {
        return repository;
    }

    @Override
    public String getCaptionFieldName() {
        return Role_.name.getName();
    }
    
}
