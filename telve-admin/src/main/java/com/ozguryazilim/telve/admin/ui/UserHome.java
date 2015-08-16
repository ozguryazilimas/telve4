/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.admin.AbstractIdentityHome;
import com.ozguryazilim.telve.admin.IdentityEvent;
import com.ozguryazilim.telve.auth.AbstractIdentityHomeExtender;
import com.ozguryazilim.telve.auth.UserModel;
import com.ozguryazilim.telve.auth.UserModelRegistery;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.RelationshipQuery;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı tanımlama Ekranı. PicketLink IDM üzerinden çalışır.
 *
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class UserHome extends AbstractIdentityHome<User> {

    private static final Logger LOG = LoggerFactory.getLogger(UserHome.class);

    @Inject
    private ViewConfigResolver viewConfigResolver;

    private DualListModel<Role> roleList;

    private List<String> fragments;
    private List<AbstractIdentityHomeExtender> extenders;

    private String userType = UserModelRegistery.getDefaultUserType();

    private List<Role> oldRoles = new ArrayList<>();
    //sistemde mevcut bulunan rollerin listesi
    private List<Role> availRoles;

    private String password;
    
    @Inject
    private RelationshipManager relationshipManager;
    
    @Inject
    private IdentityManager identityManager;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public List<User> getEntityList() {
        return getIdentityManager().createIdentityQuery(User.class).getResultList();
    }

    @Override
    public void createNew() {
        setCurrent(new User());
        doAfterNew();
    }

    public DualListModel<Role> getRoleList() {
        if (roleList == null) {
            initRoleList();
        }
        return roleList;
    }

    public void setRoleList(DualListModel<Role> roleList) {
        this.roleList = roleList;
    }

    /**
     * Kullanıcıya atanabilecek rol listesini hazırlar.
     */
    private void initRoleList() {
        List<Role> target = new ArrayList<>(oldRoles);
        List<Role> source = new ArrayList<>();
        
        for( Role r : getAvilRoles() ){
            if( !target.contains(r)){
                source.add(r);
            }
        }

        roleList = new DualListModel<>(source, target);
    }

    private List<Role> getAvilRoles(){
        if( availRoles == null ){
            availRoles = getIdentityManager().createIdentityQuery(Role.class).getResultList();
        }
        return availRoles;
    }
    
    public List<Role> getRoles() {
        return getIdentityManager().createIdentityQuery(Role.class).getResultList();
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
     * Geriye extender CDI bileşen listesini döndürür.
     *
     * @return
     */
    @Override
    public List<AbstractIdentityHomeExtender> getExtenders() {
        if (extenders == null) {
            extenders = UserModelRegistery.getExtenders();
        }
        return extenders;
    }

    /**
     * Geriye kullanıcı tiplerini döndürür.
     *
     * @return
     */
    public List<String> getUserTypes() {
        return UserModelRegistery.getUserTypes();
    }

    @Override
    protected boolean doBeforeSave() {
        //Önce kullanıcı tipini yerleştiriyoruz. Sonra da extenderlara işi bırakıyoruz.
        getCurrent().setAttribute(new Attribute<>("UserType", userType));
        return super.doBeforeSave();
    }

    @Override
    protected boolean doAfterSave() {
        //Kullanıcı bilgisi kaydedildi şimdi parola değişmiş se onu kaydedeceğiz.
        if( !Strings.isNullOrEmpty(password)){
            Password pwd = new Password(password);
            identityManager.updateCredential(getCurrent(), pwd);
            password = null;
        }
        
        //Kullanıcı bilgisi kaydeldi. Şimdide rolleri kaydedeceğiz. 

        //Yeni gelenleri ekleyelim
        for (Role r : roleList.getTarget()) {
            if (!BasicModel.hasRole(relationshipManager, getCurrent(), r)) {
                BasicModel.grantRole(relationshipManager, getCurrent(), r);
            }
        }
        
        //çıkarılmışları silelim.
        for( Role r : oldRoles ){
            
            if( !roleList.getTarget().contains(r)){
                BasicModel.revokeRole(relationshipManager, getCurrent(), r);
            }
            
        }

        //Ve sırada extenderlar var.
        return super.doAfterSave();

    }

    @Override
    protected boolean doAfterLoad() {
        //Kullanıcı tipini bir okuyalım
        Attribute<String> at = getCurrent().getAttribute("UserType");
        userType = at != null ? at.getValue() : UserModelRegistery.getDefaultUserType();

        //mevcut rolleri toplayalım
        oldRoles.clear();
        
        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
        query.setParameter(Grant.ASSIGNEE, getCurrent());
        List<Grant> result = query.getResultList();
        for (Grant grant : result) {
            oldRoles.add(grant.getRole());
        }
        
        initRoleList();
        
        return super.doAfterLoad();
    }

    @Override
    protected boolean doAfterNew() {
        oldRoles.clear();
        initRoleList();
        return super.doAfterNew(); 
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void roleDeleteListener(@Observes IdentityEvent event){
        if( !IdentityEvent.FROM_ROLE.equals(event.getFrom())) return;
        
        LOG.debug("Role Event Catch for : {}", event.getIdentity());
        
        //Action'nın ne olduğu ile ilgilenmiyoruz ama rollerimizi init etmemiz gerek
        availRoles = null;
        doCreateNew();
    }
    
}
