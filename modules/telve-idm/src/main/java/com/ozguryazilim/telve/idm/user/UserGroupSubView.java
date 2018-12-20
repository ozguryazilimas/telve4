package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.SubView;
import com.ozguryazilim.telve.forms.SubViewQueryBase;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.Group_;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserGroup_;
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
@SubView(containerPage = IdmPages.UserView.class, viewPage = IdmPages.UserGroupSubView.class, permission = "userGroup", order = 42)
public class UserGroupSubView extends SubViewQueryBase<UserGroup, UserGroupViewModel>{

    @Inject
    private UserGroupRepository repository;
    
    @Inject
    private UserHome userHome;
    
    @Inject
    private Identity identity;
    
    @Inject
    private AuditLogger auditLogger;
    
    @Inject
    private Event<UserDataChangeEvent> userEvent;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<UserGroup, UserGroupViewModel> queryDefinition) {
        queryDefinition
                .addColumn(new SubTextColumn<>(UserGroup_.group, Group_.name, "general.label.Group"), true);
    }

    @Override
    protected RepositoryBase<UserGroup, UserGroupViewModel> getRepository() {
        repository.setUser(userHome.getEntity());
        return repository;
    }
    
    @Override
    public boolean onBeforeSave(){
        getEntity().setUser( userHome.getEntity());
        return true;
    }

    public void onGroupSelect(SelectEvent event) {
        List<Group> ls = getGroups(event);
        addGroups(ls);
    }

    @Override
    public boolean onBeforeDelete() {
        auditLogger.actionLog(userHome.getEntity().getClass().getSimpleName(), userHome.getEntity().getId(), userHome.getEntity().getLoginName(), AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_DELETE, identity.getLoginName(), "User removed from group "+ getEntity().getGroup().getName());
        userEvent.fire(new UserDataChangeEvent(userHome.getEntity().getLoginName()));
        return true;
    }
    
    

    /**
     * Verilen şikayetleri muayene'ye ekler
     *
     * @param comps
     */
    protected void addGroups(List<Group> comps) {
        for (Group c : comps) {
            if (!isGroupAdded(c)) {
                UserGroup ur = new UserGroup();
                ur.setUser(userHome.getEntity());
                ur.setGroup(c);
                repository.save(ur);
                auditLogger.actionLog(userHome.getEntity().getClass().getSimpleName(), userHome.getEntity().getId(), userHome.getEntity().getLoginName(), AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_INSERT, identity.getLoginName(), "User added to group "+c.getName());
                userEvent.fire(new UserDataChangeEvent(userHome.getEntity().getLoginName()));
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
    protected boolean isGroupAdded(Group c) {
        UserGroup ur = repository.findAnyByUserAndGroup(userHome.getEntity(), c);
        return ur != null;
    }

    /**
     * Seçim eventinden complain listesini döndürür.
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Group> getGroups(SelectEvent event) {
        LookupSelectTuple tuple = (LookupSelectTuple) event.getObject();

        List<Group> ls = new ArrayList<>();
        if (tuple != null) {
            if (tuple.getValue() instanceof List) {
                ls.addAll((List<Group>) tuple.getValue());
            } else {
                ls.add((Group) tuple.getValue());
            }
        }

        return ls;
    }
    
    public Boolean canInsert(){
        return userHome.canChangeCriticalData();
    }
}
