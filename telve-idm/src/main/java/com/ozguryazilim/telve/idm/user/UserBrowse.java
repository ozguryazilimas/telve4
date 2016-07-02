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
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.Group_;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup_;
import com.ozguryazilim.telve.idm.entities.User_;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.LinkColumn;
import com.ozguryazilim.telve.query.columns.MessageColumn;
import com.ozguryazilim.telve.query.columns.TextColumn;
import com.ozguryazilim.telve.query.filters.BooleanFilter;
import com.ozguryazilim.telve.query.filters.FilterOperand;
import com.ozguryazilim.telve.query.filters.StringFilter;
import com.ozguryazilim.telve.query.filters.TreeEntityFilter;
import javax.inject.Inject;

/**
 * User Browse Controller
 * 
 * 
 * User -> UserGroup -> Group arasında doğrudan bir ilişki olmadığı için Bir grup içindeki kullanıcıları filtrelemek doğrudan browse filter ile mümkün değil.
 * 
 * Dolayısı ile extraFilter olarak bir filtre gönderip Repository üzerinde özel bi sorgu ile ( exists ) bu kontol yapılacak. 
 * Bunun için de özel bir quick filter ekleyeceğiz ara yüze.
 * 
 * @author Hakan Uygun
 */
@Browse(browsePage = IdmPages.UserBrowse.class, editPage = IdmPages.User.class, viewContainerPage = IdmPages.UserView.class)
public class UserBrowse extends BrowseBase<User, UserViewModel>{

    @Inject
    private UserRepository repository;
    
    private TreeEntityFilter groupFilter;
    
    private Group selectedGroup;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<User, UserViewModel> queryDefinition) {
        groupFilter = new TreeEntityFilter<>(UserGroup_.group, Group_.path, null, "");
        groupFilter.setOperand(FilterOperand.Under);
        
        queryDefinition
                .addFilter(new StringFilter<>(User_.loginName, "general.label.LoginName"))
                .addFilter(new StringFilter<>(User_.firstName, "general.label.FirstName"))
                .addFilter(new StringFilter<>(User_.lastName, "general.label.LastName"))
                .addFilter(new BooleanFilter<>(User_.active, "general.label.Active", "general.boolean.yesno."))
                .addFilter(new BooleanFilter<>(User_.autoCreated, "user.label.LDAP", "general.boolean.yesno."));
                
                
        queryDefinition
                .addColumn(new LinkColumn<>(User_.loginName, "general.label.LoginName"), true)
                .addColumn(new TextColumn<>(User_.firstName, "general.label.FirstName"), true)
                .addColumn(new TextColumn<>(User_.lastName, "general.label.LastName"), true)
                .addColumn(new MessageColumn<>(User_.userType, "user.label.UserType", "userType.label."), true)
                .addColumn(new TextColumn<>(User_.email, "general.label.Email"), true);
        
        queryDefinition.addExtraFilter(groupFilter);
    }

    @Override
    protected RepositoryBase<User, UserViewModel> getRepository() {
        return repository;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
        groupFilter.setValue(selectedGroup);
        search();
    }
    
    
}
