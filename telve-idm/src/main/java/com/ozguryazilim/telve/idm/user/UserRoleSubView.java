/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.SubView;
import com.ozguryazilim.telve.forms.SubViewQueryBase;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.Role_;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.entities.UserRole_;
import com.ozguryazilim.telve.lookup.LookupSelectTuple;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.SubTextColumn;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author oyas
 */
@SubView(containerPage = IdmPages.UserView.class, viewPage = IdmPages.UserRoleSubView.class, permission = "userRole", order = 42)
public class UserRoleSubView extends SubViewQueryBase<UserRole, UserRoleViewModel>{
    
    @Inject
    private UserRoleRepository repository;
    
    @Inject
    private UserHome userHome;

    @Inject
    private Identity identity;    
    
    @Inject
    private AuditLogger auditLogger;
    
    @Inject
    private Event<UserDataChangeEvent> userEvent;

    @Override
    protected void buildQueryDefinition(QueryDefinition<UserRole, UserRoleViewModel> queryDefinition) {
        queryDefinition
                .addColumn(new SubTextColumn<>(UserRole_.role, Role_.name, "general.label.Role"), true);
    }

    @Override
    protected RepositoryBase<UserRole, UserRoleViewModel> getRepository() {
        repository.setUser(userHome.getEntity());
        return repository;
    }
    
    @Override
    public boolean onBeforeDelete() {
        auditLogger.actionLog(userHome.getEntity().getClass().getSimpleName(), userHome.getEntity().getId(), userHome.getEntity().getLoginName(), AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_DELETE, identity.getLoginName(), "User role "+ getEntity().getRole().getName() + " removed");
        userEvent.fire(new UserDataChangeEvent(userHome.getEntity().getLoginName()));
        return true;
    }

    @Override
    public boolean onAfterSave() {
        userEvent.fire(new UserDataChangeEvent(userHome.getEntity().getLoginName()));
        return super.onAfterSave();
    }
    
    
    
    @Override
    public boolean onBeforeSave(){
        getEntity().setUser( userHome.getEntity());
        return true;
    }
    
    
    public void onRoleSelect(SelectEvent event) {
        List<Role> ls = getRoles(event);
        addComplains(ls);
    }

    /**
     * Verilen şikayetleri muayene'ye ekler
     *
     * @param comps
     */
    protected void addComplains(List<Role> comps) {
        for (Role c : comps) {
            if (!isRoleAdded(c)) {
                UserRole ur = new UserRole();
                ur.setUser(userHome.getEntity());
                ur.setRole(c);
                repository.save(ur);
                auditLogger.actionLog(userHome.getEntity().getClass().getSimpleName(), userHome.getEntity().getId(), userHome.getEntity().getLoginName(), AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_INSERT, identity.getLoginName(), "Role "+c.getName() + " given to User " + userHome.getEntity().getLoginName());
            }
        }
        search();
    }

    /**
     * Verilen rolün daha önce listeye eklenip eklenmediğine bakar.
     *
     * @param c
     * @return
     */
    protected boolean isRoleAdded(Role c) {
        UserRole ur = repository.findAnyByUserAndRole(userHome.getEntity(), c);
        return ur != null;
    }

    /**
     * Seçim eventinden complain listesini döndürür.
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Role> getRoles(SelectEvent event) {
        LookupSelectTuple tuple = (LookupSelectTuple) event.getObject();

        List<Role> ls = new ArrayList<>();
        if (tuple != null) {
            if (tuple.getValue() instanceof List) {
                ls.addAll((List<Role>) tuple.getValue());
            } else {
                ls.add((Role) tuple.getValue());
            }
        }

        return ls;
    }

    
}
