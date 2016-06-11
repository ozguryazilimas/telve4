/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.Condition;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.RelationshipQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı bilgileri Lookup kontrol sınıfı.
 * 
 * PicketLink IdentityManager'ı üzerinden kullanıcı bilgileri döndürür.
 * 
 * Aktif kullanıcı ile bilgileri almak için UserInfo modelini kullanınız. UserInfoProducer üzerinden SessionScoped üretilir.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class UserLookup implements Serializable{
    
    
    private static final Logger LOG = LoggerFactory.getLogger(UserLookup.class);
    
    private static final String USER_TYPE = "UserType"; 
    private static final String USER_GROUP = "UserGroup"; 
    
    @Inject
    private IdentityManager identityManager;
    
    @Inject
    private RelationshipManager relationshipManager;
    
    @Inject
    private Identity identity;
    
    /**
     * Geriye sistemde tanımlı kullanıcı login isimlerini döndürür.
     * 
     * @return 
     */
    public List<String> getLoginNames(){
        
        List<String> result = new ArrayList<>();
        List<User> ls = getUsers();
        
        for( User u : ls ){
            result.add(u.getLoginName());
        }
        
        return result;
    }
    
    /**
     * Geriye sistemde tanımlı kullanıcı isimlerini döndürür.
     * 
     * @return 
     */
    public List<User> getUsers(){
        return identityManager.createIdentityQuery(User.class).getResultList();
    }
    
    /**
     * Kullanıcı tipine göre kullanıcı listesi döndürür.
     * 
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir.
     * 
     * @param type
     * @return 
     */
    public List<User> getUsersByType( String type ){
        
        List<User> result = new ArrayList();
        
        if( type.contains(",")){
            List<String> ls = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(type);
            for( String s : ls ){
                result.addAll( getUsersByTypeImpl(s) );
            }
        } else {
            result.addAll( getUsersByTypeImpl(type) );
        }
        
        return result;
    }
    
    
    /**
     * Tek bir userType için sorgu sonucu döndürür.
     * 
     * Aslında  c = builder.in(IdentityType.QUERY_ATTRIBUTE.byName(USER_TYPE), ls.toArray(new String[0])); yapılması lazım ama doğru çalışmıyor.
     * 
     * @param type
     * @return 
     */
    protected List<User> getUsersByTypeImpl( String type ){
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        Condition c = builder.equal(IdentityType.QUERY_ATTRIBUTE.byName(USER_TYPE), type);
        
        return builder.createIdentityQuery(User.class)
                .where(c)
                .getResultList();
    }
            
    
    /**
     * Kullanıcı grubuna göre kullanıcı listesi döndürür.
     * 
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir.
     * 
     * @param group
     * @return 
     */
    public List<User> getUsersByGroup( String group ){
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        
        Condition c = builder.equal(IdentityType.QUERY_ATTRIBUTE.byName(USER_GROUP), group);
        
        return builder.createIdentityQuery(User.class)
                .where(c)
                .getResultList();
    }
    
    /**
     * Kullanıcı tipine ve grup'a göre kullanıcı listesi döndürür.
     * 
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir.
     * 
     * @param type
     * @param group
     * @return 
     */
    public List<User> getUsersByTypeAndGroup( String type, String group ){
        List<User> result = new ArrayList();
        
        if( type.contains(",")){
            List<String> ls = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(type);
            for( String s : ls ){
                result.addAll( getUsersByTypeAndGroupImpl(s, group) );
            }
        } else {
            result.addAll( getUsersByTypeAndGroupImpl(type, group) );
        }
        
        return result;
    }
    
    protected List<User> getUsersByTypeAndGroupImpl( String type, String group ){
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        
        Condition c1 = builder.equal(IdentityType.QUERY_ATTRIBUTE.byName(USER_TYPE), type);
        Condition c2 = builder.equal(IdentityType.QUERY_ATTRIBUTE.byName(USER_GROUP), group);
        
        return builder.createIdentityQuery(User.class)
                .where(c1, c2)
                .getResultList();
    }
    
    /**
     * Verilen tip boş ise tüm kullanıcıları değil ise verilen tipteki kullanıcıları döndürür.
     * @param type
     * @return 
     */
    public List<User> getUsers( String type ){
        if( Strings.isNullOrEmpty(type)){
            return getUsers();
        } else {
            return getUsersByType(type);
        }
    }
    
    /**
     * Tip ve Grup kontrolü ile kullanıcı listesi döndürür.
     * 
     * Eğer verilen değerler boş ise tüm kullanıcıları döndürür.
     * 
     * @param type
     * @param group
     * @return 
     */
    public List<User> getUsers( String type, String group ){
        if( Strings.isNullOrEmpty(type) && !Strings.isNullOrEmpty(group)) {
            return getUsersByGroup(group);
        } else if( !Strings.isNullOrEmpty(type) && Strings.isNullOrEmpty(group)) {
            return getUsersByType(type);
        } else if( !Strings.isNullOrEmpty(type) && !Strings.isNullOrEmpty(group)) {
            return getUsersByTypeAndGroup(type, group);
        } else {
            return getUsers();
        }
    }
    
    /**
     * Verilen Login Name'e sahip kullanıcının gerçek adını döndürür.
     * @param loginName ismi döndürülecek loginName
     * @return bulamazsa geriye null döndürür.
     */
    public String getUserName( String loginName ){
        List<User> ls = identityManager.createIdentityQuery(User.class)
                .setParameter(User.LOGIN_NAME, loginName)
                .getResultList();
        
        if( !ls.isEmpty() ){
            return ls.get(0).getFirstName() + " " + ls.get(0).getLastName();
        }
        
        return null;
    }
    
    /**
     * Loginname'e göre UserInfo modelini döner.
     * @param loginName
     * @return 
     */
    public UserInfo getUserInfo( String loginName ){
        List<User> ls = identityManager.createIdentityQuery(User.class)
                .setParameter(User.LOGIN_NAME, loginName)
                .getResultList();
        
        if( !ls.isEmpty() ){
            UserInfo ui = new UserInfo();
            ui.setId(ls.get(0).getId());
            ui.setFirstName(ls.get(0).getFirstName());
            ui.setLastName(ls.get(0).getLastName());
            ui.setLoginName(ls.get(0).getLoginName());
            return ui;
        }
        
        return null;
    }
    
    /**
     * Loginname'e göre User modelini döner.
     * @param loginName
     * @return 
     */
    public User getUser( String loginName ){
        List<User> ls = identityManager.createIdentityQuery(User.class)
                .setParameter(User.LOGIN_NAME, loginName)
                .getResultList();
        
        if( !ls.isEmpty() ){
            return ls.get(0);
        }
        
        return null;
    }
    
    
    /**
     * Verilen ID için login name'i döndrür.
     * @param id
     * @return 
     */
    public String getLoginName( String id ){
        User u = identityManager.lookupIdentityById(User.class, id);
        return u.getLoginName();
    }
    
    /**
     * Verilen ID'e sahip kullanıcının gerçek adını döndürür.
     * @param id ismi döndürülecek kullanıcı PL id'si
     * @return bulamazsa geriye null döndürür.
     */
    public String getUserNameById( String id ){
        User u = identityManager.lookupIdentityById(User.class, id);
        
        if( u != null ){
            return u.getFirstName() + " " + u.getLastName();
        }
        
        return null;
    }
    
    public UserInfo getUserInfoById( String id ){
        User u = identityManager.lookupIdentityById(User.class, id);
        
        if( u != null ){
            UserInfo ui = new UserInfo();
            ui.setId(u.getId());
            ui.setFirstName(u.getFirstName());
            ui.setLastName(u.getLastName());
            ui.setLoginName(u.getLoginName());
            return ui;
        }
        
        return null;
    }
    
    /**
     * Verilen Kullanıcı için role isim listesini döndürür.
     * 
     * addNone true ise liste boş ise içine 'NONE' değerini ekler.
     * 
     * @param user
     * @param addNone
     * @return 
     */
    public List<String> getUserRoleNames( User user, boolean addNone ){
        List<String> roleNames = new ArrayList<>();
        
        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);
        query.setParameter(Grant.ASSIGNEE, user);
        List<Grant> result = query.getResultList();
        for (Grant grant : result) {
            roleNames.add(grant.getRole().getName());
        }
        
        //Eğer boşsa içine none ekliyoruz. Böylece IN içine konan sorgular hata vermeyecek.
        if( roleNames.isEmpty() && addNone ){
            roleNames.add("NONE");
        }
        
        return roleNames;
    }
    
    /**
     * İsmi verilen kullanıcı için istenilen attribute değerini döner.
     * @param userName
     * @param attribute
     * @return 
     */
    public String getUserAttibute( String userName, String attribute ){
        User u = getUser(userName);
        Attribute<String> a = u.getAttribute(attribute);
        return a == null ? null : a.getValue();
    }
    


    
   
}
