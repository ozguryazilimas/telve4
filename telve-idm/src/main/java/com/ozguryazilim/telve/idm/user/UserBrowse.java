/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.Browse;
import com.ozguryazilim.telve.forms.BrowseBase;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.User_;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.LinkColumn;
import com.ozguryazilim.telve.query.columns.TextColumn;
import com.ozguryazilim.telve.query.filters.StringFilter;
import javax.inject.Inject;

/**
 * User Browse Controller
 * 
 * @author Hakan Uygun
 */
@Browse(browsePage = IdmPages.UserBrowse.class, editPage = IdmPages.User.class, viewContainerPage = IdmPages.UserView.class)
public class UserBrowse extends BrowseBase<User, UserViewModel>{

    @Inject
    private UserRepository repository;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<User, UserViewModel> queryDefinition) {
        queryDefinition
                .addFilter(new StringFilter<>(User_.lastName, "general.label.Name"))
                .addFilter(new StringFilter<>(User_.firstName, "general.label.FirstName"));
        queryDefinition
                .addColumn(new LinkColumn<>(User_.loginName, "general.label.Serial"), true)
                .addColumn(new LinkColumn<>(User_.lastName, "general.label.Serial"), true)
                .addColumn(new TextColumn<>(User_.firstName, "general.label.Subject"), true, false);
        
    }

    @Override
    protected RepositoryBase<User, UserViewModel> getRepository() {
        return repository;
    }
    
}
