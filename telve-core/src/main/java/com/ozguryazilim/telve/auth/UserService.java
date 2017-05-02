/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Telve alt yapısında kullanılan providerlaraın sağladığı bilgiler için temel
 * API sağlar.
 *
 * Aynı zamanda topladığı bilgileri cache içerisinde tutar.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class UserService {

    @Inject
    @Any
    private Instance<UserServiceProvider> userServiceProviders;

    @Inject
    @Any
    private Instance<UserRoleResolver> userRoleResolvers;

    private Map<String, String> userNames = new HashMap<>();
    private Map<String, List<String>> userRoles = new HashMap<>();
    private Map<String, List<String>> userGroups = new HashMap<>();
    private Map<String, List<String>> userGroupsMembers = new HashMap<>();
    private Map<String, List<String>> userUnifiedRoles = new HashMap<>();
    private Map<String, Map<String, String>> userAttrs = new HashMap<>();
    private Map<String, UserInfo> userInfos = new HashMap<>();

    /**
     * Geriye var ise login olan mevcut kullanıcının bilgisini döndürür.
     *
     * @return
     */
    private String getCurrentUser() {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser != null) {
            return currentUser.getPrincipal().toString();
        }

        return "";
    }

    /**
     * Eğer aktif bir kullanıcı var ise o kullanıcının yetkili ( domainGroup ) olduğu kullanıcıların listesini döndürür.
     *
     * Eğer aktif kullanıcı yoksa bütün kullanıcı listesini döndürür.
     * @return
     */
    public List<String> getLoginNames() {
        List<String> result = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            result.addAll(usp.getLoginNames());
        }

        return result;
    }

    /**
     * Kullanıcı tipine göre kullanıcı ( loginName ) listesi döndürür.
     *
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir
     * 
     * Kullanıcı tipleri virgüller ile ayrılmış olarak gelebilir. Böylece bir den fazla tipte kullanıcı çekilebilir.
     *
     * @param type
     * @return
     */
    public List<String> getUsersByType(String type) {
        List<String> result = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            result.addAll(usp.getUsersByType(type));
        }

        return result;
    }

    /**
     * Verilen Grup üyesi kullanıcı listesini döndürür.
     *
     * Grup yapısı ağaç ise o ağacın alt dallarında olan kullanıcıları da
     * döndürür. Eğer ağaç bir yapı ise parametre olarak Path verilmelidir.
     *
     * @param group
     * @return
     */
    public List<String> getUsersByGroup(String group) {

        List<String> result = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            result.addAll(usp.getUsersByGroup(group));
        }

        return result;
    }
    
    
    public List<String> getUsersByTypeAndGroup(String type, String group) {

        List<String> result = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            result.addAll(usp.getUsersByTypeAndGroup(type, group));
        }

        return result;
    }
    
    
    public List<String> getUsersByTextAndTypeAndGroup( String searchText, String type, String group) {
        List<String> result = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            result.addAll(usp.getUsersByTextAndTypeAndGroup( searchText, type, group));
        }

        return result;
    }

    /**
     * LoginName'i kullanıcı adına çevirir.
     *
     *
     * @param loginName
     * @return
     */
    public String getUserName(String loginName) {
        String s = userNames.get(loginName);
        if (Strings.isNullOrEmpty(s)) {
            //İlk bulduğu user name bilgisini çevirir.
            for (UserServiceProvider usp : userServiceProviders) {
                s = usp.getUserName(loginName);
                if (!Strings.isNullOrEmpty(s)) {
                    userNames.put(loginName, s);
                    break;
                }
            }
            return s;
        }

        return s;
    }

    /**
     * Login name verilen kullanıcının role listesini döndürür.
     *
     * @param loginName
     * @return
     */
    public List<String> getUserRoles(String loginName) {
        List<String> ls = userRoles.get(loginName);

        if (ls == null) {
            ls = populateUserRoles(loginName);
            userRoles.put(loginName, ls);
        }

        return ls;
    }

    /**
     * Login name verilen kullanıcının role listesini döndürür.
     *
     * @param loginName
     * @return
     */
    public List<String> getUserUnifiedRoles(String loginName) {
        List<String> ls = userUnifiedRoles.get(loginName);

        if (ls == null) {
            ls = populateUserUnifiedRoles(loginName);
            userUnifiedRoles.put(loginName, ls);
        }

        return ls;
    }

    /**
     * Login name verilen kullanıcının grup isim listesini döndürür.
     *
     * @param loginName
     * @return
     */
    public List<String> getUserGroups(String loginName) {
        List<String> ls = userGroups.get(loginName);

        if (ls == null) {
            ls = populateUserGroups(loginName);
            userGroups.put(loginName, ls);
        }

        return ls;
    }
    
    public List<String> getUserGroupsMembers( String loginName){
        List<String> ls = userGroupsMembers.get(loginName);

        if (ls == null) {
            ls = populateUserGroupsMembers(loginName);
            userGroupsMembers.put(loginName, ls);
        }

        return ls;
    }

    /**
     * İsmi verilen kullanıcının varsa ismi verilen attribute'unu döndürür.
     *
     * @param loginName
     * @param attribute
     * @return
     */
    public String getUserAttibute(String loginName, String attribute) {
        Map<String, String> map = getUserAttibutes(loginName);

        return map.get(attribute);
    }

    /**
     * İsmi verilen kullanıcının verilen listede bulunana attributelarını
     * döndürür.
     *
     * @param loginName
     * @param attributes
     * @return
     */
    public Map<String, String> getUserAttibutes(String loginName) {

        Map<String, String> result = userAttrs.get(loginName);
        if (result == null) {
            result = populateUserAttibutes(loginName);
            userAttrs.put(loginName, result);
            return result;
        }

        return result;
    }

    public UserInfo getUserInfo(String loginName) {
        UserInfo ui = userInfos.get(loginName);

        if (ui == null) {
            ui = populateUserInfo(loginName);
            if (ui != null) {
                userInfos.put(loginName, ui);
            }
        }

        return ui;
    }

    private List<String> populateUsersByType(String type) {
        List<String> ls = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            ls.addAll(usp.getUsersByType(type));
        }

        return ls;
    }

    private UserInfo populateUserInfo(String loginName) {
        for (UserServiceProvider usp : userServiceProviders) {
            UserInfo ui = usp.getUserInfo(loginName);
            if (ui != null) {
                return ui;
            }
        }

        return null;
    }

    private List<String> populateUsersByGroup(String group) {
        List<String> ls = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            ls.addAll(usp.getUsersByGroup(group));
        }

        return ls;
    }

    private List<String> populateUserRoles(String loginName) {
        List<String> ls = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            ls.addAll(usp.getUserRoles(loginName));
        }

        return ls;
    }

    private List<String> populateUserGroups(String loginName) {
        List<String> ls = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            ls.addAll(usp.getUserGroups(loginName));
        }

        return ls;
    }
    
    private List<String> populateUserGroupsMembers(String loginName) {
        List<String> ls = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            ls.addAll(usp.getUserGroupsMembers(loginName));
        }

        return ls;
    }

    /**
     * İsmi verilen kullanıcının verilen listede bulunana attributelarını
     * döndürür.
     *
     * @param loginName
     * @param attributes
     * @return
     */
    private Map<String, String> populateUserAttibutes(String loginName) {
        Map<String, String> result = new HashMap<>();

        for (UserServiceProvider usp : userServiceProviders) {
            result.putAll(usp.getUserAttibutes(loginName));
        }

        return result;
    }

    /**
     * Kullanıcıya ait role, user id, group id, ve uygulamalardan gelebilecek
     * örneğin organizasyon bilgilerini bir araya toparlar.
     *
     * Farklı türler için birer harf kullanır :
     *
     * Reservd olanlar : U: User id L: Login name T: User Type R: Role G: Group
     *
     * Örnek : U:telve R:superuser G:Admins
     *
     */
    private List<String> populateUserUnifiedRoles(String loginName) {
        List<String> result = new ArrayList<>();

        //Şimdide uygulamalardan gelenler toparlanıyor. Bir den fazla resolver olabilir.
        for (UserRoleResolver urr : userRoleResolvers) {
            result.addAll(urr.getUnifiedRoles(loginName));
        }

        return result;
    }
    
    public void clearCache( @Observes UserDataChangeEvent event){
        userNames.remove(event.username);
        userRoles.remove(event.username);
        userGroups.remove(event.username);
        userUnifiedRoles.remove(event.username);
        userAttrs.remove(event.username);
        userInfos.remove(event.username);
    }
}
