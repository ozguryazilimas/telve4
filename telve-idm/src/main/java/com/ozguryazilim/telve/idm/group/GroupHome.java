/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.group;

import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.data.TreeRepositoryBase;
import com.ozguryazilim.telve.forms.ParamEdit;
import com.ozguryazilim.telve.forms.TreeBase;
import com.ozguryazilim.telve.idm.IdmEvent;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.lookup.LookupSelectTuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author oyas
 */
@ParamEdit
public class GroupHome extends TreeBase<Group>{

    @Inject
    private GroupRepository repository;
    
    @Inject
    private UserGroupRepository userGrouprepository;
    
    @Inject 
    private Event<IdmEvent>  event;
    
    @Override
    protected TreeRepositoryBase<Group> getRepository() {
        return repository;
    }
    
    protected Group getParent() {
        Group parent = null;
        if (getTreeModel().getSelectedData() != null) {
            parent = getTreeModel().getSelectedData();
        }
        return parent;
    }

    public void newGroup() {
        setEntity(repository.newGroup(getParent()));
    }

    public void newRootGroup() {
        setEntity(repository.newGroup(null));
    }
    
    
    @Override
    protected boolean onBeforeSave() {
        getEntity().setCode(getEntity().getName());
        return true;
    }
    
    public List<UserGroup> getMembers(){
        if( !getEntity().isPersisted()) {
            return Collections.emptyList();
        }
        return userGrouprepository.findByGroup(getEntity());
    }
    
    
    public void onUserSelect(SelectEvent event) {
        List<User> ls = getUsers(event);
        addUsers(ls);
    }

    /**
     * Verilen şikayetleri muayene'ye ekler
     *
     * @param comps
     */
    protected void addUsers(List<User> comps) {
        for (User c : comps) {
            if (!isUserAdded(c)) {
                UserGroup ur = new UserGroup();
                ur.setUser(c);
                ur.setGroup(getEntity());
                userGrouprepository.save(ur);
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
    protected boolean isUserAdded(User c) {
        UserGroup ur = userGrouprepository.findAnyByUserAndGroup(c, getEntity());
        return ur != null;
    }

    /**
     * Seçim eventinden complain listesini döndürür.
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<User> getUsers(SelectEvent event) {
        LookupSelectTuple tuple = (LookupSelectTuple) event.getObject();

        List<User> ls = new ArrayList<>();
        if (tuple != null) {
            if (tuple.getValue() instanceof List) {
                ls.addAll((List<User>) tuple.getValue());
            } else {
                ls.add((User) tuple.getValue());
            }
        }

        return ls;
    }
    
    @Transactional
    public void removeItem( Long id ){
        UserGroup ug = userGrouprepository.findBy(id);
        userGrouprepository.remove(ug);
    }

    @Override
    protected boolean onAfterSave() {
        event.fire(new IdmEvent(IdmEvent.FROM_GROUP, IdmEvent.CREATE, getEntity().getName()));
        return super.onAfterSave(); 
    }

    @Override
    protected void onBeforeDelete() {
        event.fire(new IdmEvent(IdmEvent.FROM_GROUP, IdmEvent.DELETE, getEntity().getName()));
        super.onAfterDelete();
    }

    @Override
    protected String getAuditLogCategory() {
        return AuditLogCommand.CAT_AUTH;
    }

    /**
     * Multi Group kullanımı aktif ise true aksi halde false döner.
     * @return 
     */
    public boolean getIsMultiGroupActive(){
        return "true".equals(ConfigResolver.getPropertyValue("security.multiGroup.control", "false"));
    }
    
}
