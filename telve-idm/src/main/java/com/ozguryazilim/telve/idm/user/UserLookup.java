/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.User_;
import com.ozguryazilim.telve.lookup.Lookup;
import com.ozguryazilim.telve.lookup.LookupTableControllerBase;
import com.ozguryazilim.telve.lookup.LookupTableModel;
import javax.inject.Inject;

/**
 *
 * @author oyas
 */
@Lookup(dialogPage = IdmPages.UserLookup.class)
public class UserLookup extends LookupTableControllerBase<User, UserViewModel>{

    @Inject
    private UserRepository repository;
    
    @Override
    protected void buildModel(LookupTableModel<UserViewModel> model) {
        model.addColumn("loginName", "general.label.LoginName");
        model.addColumn("firstName", "general.label.FirstName");
        model.addColumn("lastName", "general.label.LastName");
        model.addColumn("userType", "general.label.UserType");
    }

    @Override
    protected RepositoryBase<User, UserViewModel> getRepository() {
        return repository;
    }

    @Override
    public String getCaptionFieldName() {
        return User_.loginName.getName();
    }
    
}
