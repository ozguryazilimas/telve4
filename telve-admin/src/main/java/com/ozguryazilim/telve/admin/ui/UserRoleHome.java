/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.ui;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.admin.AbstractIdentityHome;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.audit.ChangeLogStore;
import com.ozguryazilim.telve.auth.AbstractIdentityHomeExtender;
import com.ozguryazilim.telve.auth.ActiveUserLookup;
import com.ozguryazilim.telve.entities.AuditLogDetail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.Condition;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı Role Yetkilendirme ekranı.
 * 
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class UserRoleHome extends AbstractIdentityHome<User>{

    private static final Logger LOG = LoggerFactory.getLogger(UserRoleHome.class);

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    private AuditLogger auditLogger;
    
    @Inject
    private ActiveUserLookup userLookup;
    
    private DualListModel<Role> roleList;
    
    private List<Role> oldRoles = new ArrayList<>();
    //sistemde mevcut bulunan rollerin listesi
    private List<Role> availRoles;

    //List<AuditLogDetail> values = new ArrayList<>();
    ChangeLogStore changeLogStore = new ChangeLogStore();
    
    @Inject
    private RelationshipManager relationshipManager;
    
    @Inject
    private IdentityManager identityManager;
    
    private String filterGroup = "ALL";

    
    public String getFilterGroup() {
        return filterGroup;
    }

    public void setFilterGroup(String filterGroup) {
        this.filterGroup = filterGroup;
    }
    
    @Override
    public List<User> getEntityList() {
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        if( Strings.isNullOrEmpty(filterGroup) || "ALL".equals(filterGroup)){
            return builder.createIdentityQuery(User.class)
                    .sortBy(builder.asc(User.LOGIN_NAME))
                    .getResultList();
        } else {
            Condition c = builder.equal(IdentityType.QUERY_ATTRIBUTE.byName("UserGroup"), filterGroup);

            return builder.createIdentityQuery(User.class)
                    .where(c)
                    .sortBy(builder.asc(User.LOGIN_NAME))
                    .getResultList();
        }
        
    }

    @Override
    public List<AbstractIdentityHomeExtender> getExtenders() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void createNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            availRoles = getIdentityManager().createIdentityQuery(Role.class)
                    .sortBy(getIdentityManager().getQueryBuilder().asc(Role.NAME))
                    .getResultList();
        }
        return availRoles;
    }
    
    public List<Role> getRoles() {
        return getIdentityManager().createIdentityQuery(Role.class)
                .sortBy(getIdentityManager().getQueryBuilder().asc(Role.NAME))
                .getResultList();
    }
    
    
    @Override
    protected boolean doAfterSave() {
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

        List<AuditLogDetail> changes = buildChangeLog();
        
        auditLogger.authLog( getCurrent().getClass().getSimpleName(), getCurrent().getLoginName(), userLookup.getActiveUser().getLoginName(), getCurrent().getFirstName() + " " + getCurrent().getLastName() + " kullanıcısının bilgileri değişti", changes );
        
        //Ve sırada extenderlar var.
        return super.doAfterSave();

        
    }


    @Override
    protected boolean doAfterLoad() {
        
        //Change log için verileri toplayalım.
        changeLogStore.clear();
        
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
    
    /**
     * Geriye değişen veri logunu hazırlayıp döndürür.
     * 
     * @return 
     */
    protected List<AuditLogDetail> buildChangeLog(){
        List<AuditLogDetail> changes = new ArrayList<>();
        
        //Önce chageStore
        changes.addAll( changeLogStore.getChangeValues());
        
        //Kullanıcı rol değişimi
        AuditLogDetail auDet = new AuditLogDetail();
        auDet.setAttribute("general.label.Roles");

        //Eski roller
        List<String> rl = new ArrayList<>();
        for( Role r : oldRoles ){
            rl.add( r.getName() ); 
        }
        auDet.setOldValue(Joiner.on(',').join(rl));
        
        //yeni roller
        rl = new ArrayList<>();
        for( Role r : roleList.getTarget() ){
            rl.add( r.getName() ); 
        }
        auDet.setNewValue(Joiner.on(',').join(rl));
        
        //Eğer veride değişiklik yoksa ekleme
        if( !auDet.getOldValue().equals(auDet.getNewValue()) ){
            changes.add(auDet);
        }
        
        return changes;
    }
}
