/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.ChangeLogStore;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.auth.UserModel;
import com.ozguryazilim.telve.auth.UserModelRegistery;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.FormBase;
import com.ozguryazilim.telve.forms.FormEdit;
import com.ozguryazilim.telve.idm.IdmEvent;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı tanımlama ekranı.
 * 
 * @author Hakan Uygun
 */
@FormEdit(browsePage = IdmPages.UserBrowse.class, editPage = IdmPages.User.class, viewContainerPage = IdmPages.UserView.class, masterViewPage = IdmPages.UserMasterView.class)
public class UserHome extends FormBase<User, Long>{
    
    private static final Logger LOG = LoggerFactory.getLogger(UserHome.class);

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private UserRepository repository;

    @Inject 
    private Event<IdmEvent>  event;
    
    @Inject
    private Event<UserDataChangeEvent> userEvent;
    
    @Inject
    private Identity identity;

    private String password;
    
    private List<String> fragments;
    
    private ChangeLogStore changeLogStore = new ChangeLogStore();

    @Override
    public boolean onAfterLoad() {
        changeLogStore.clear();
        
        changeLogStore.addOldValue("general.label.FirstName", getEntity().getFirstName());
        changeLogStore.addOldValue("general.label.LastName", getEntity().getLastName());
        changeLogStore.addOldValue("user.label.UserType", getEntity().getUserType());
        changeLogStore.addOldValue("general.label.Email", getEntity().getEmail());
        changeLogStore.addOldValue("user.label.DomainGroup", getEntity().getDomainGroup() != null ? getEntity().getDomainGroup().getName() : null );
        
        return true;
    }

    
    
    @Override
    public boolean onBeforeSave() {
        
        if( !Strings.isNullOrEmpty(password)){
            DefaultPasswordService passwordService = new DefaultPasswordService();
            getEntity().setPasswordEncodedHash(passwordService.encryptPassword(password));
            changeLogStore.addNewValue("user.caption.Password", "Changed");
        }
        
        changeLogStore.addNewValue("general.label.FirstName", getEntity().getFirstName());
        changeLogStore.addNewValue("general.label.LastName", getEntity().getLastName());
        changeLogStore.addNewValue("user.label.UserType", getEntity().getUserType());
        changeLogStore.addNewValue("general.label.Email", getEntity().getEmail());
        changeLogStore.addNewValue("user.label.DomainGroup", getEntity().getDomainGroup() != null ? getEntity().getDomainGroup().getName() : null );
        
        
        return true;
    }

    @Override
    public boolean onAfterSave() {
        event.fire(new IdmEvent(IdmEvent.FROM_USER, IdmEvent.CREATE, getEntity().getLoginName()));
        userEvent.fire(new UserDataChangeEvent(getEntity().getLoginName()));
        return super.onAfterSave(); 
    }
    
    @Override
    public boolean onBeforeDelete() {
        event.fire(new IdmEvent(IdmEvent.FROM_USER, IdmEvent.DELETE, getEntity().getLoginName()));
        userEvent.fire(new UserDataChangeEvent(getEntity().getLoginName()));
        return super.onAfterSave(); 
    }
    
    

    /**
     * Geriye ek model UI fragmentlerinin listesi döner.
     *
     * @return
     */
    public List<String> getUIFragments() {

        if (fragments == null) {
            populateFragments();
        }

        LOG.info("UI Fragments : {}", fragments);

        return fragments;
    }

    /**
     * UI için gerekli fragment listesini hazırlar.
     */
    protected void populateFragments() {
        fragments = new ArrayList<>();

        for (UserModel m : UserModelRegistery.getUserModelMap().values()) {
            fragments.add(viewConfigResolver.getViewConfigDescriptor(m.fragment()).getViewId());
        }
    }

    
    /**
     * Geriye kullanıcı tiplerini döndürür.
     *
     * @return
     */
    public List<String> getUserTypes() {
        return UserModelRegistery.getUserTypes();
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDomainGroupRequired(){
        return "true".equals(ConfigResolver.getPropertyValue("security.domainGroup.control", "false"));
    }
    
    @Override
    protected RepositoryBase<User, ?> getRepository() {
        return repository;
    }    

    
    @Override
    protected void auditLog(String action) {
        getAuditLogger().actionLog(getEntity().getClass().getSimpleName(), getEntity().getId(), getBizKeyValue(), AuditLogCommand.CAT_AUTH,  action, identity.getLoginName(), "", changeLogStore.getChangeValues());
    }
    
    
}
