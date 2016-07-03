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
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

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

    private List<String> loginNames;
    private Map<String, List<String>> userByTypes = new HashMap<>();
    private Map<String, List<String>> userByGroups = new HashMap<>();
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, List<String>> userRoles = new HashMap<>();
    private Map<String, List<String>> userGroups = new HashMap<>();
    private Map<String, List<String>> userUnifiedRoles = new HashMap<>();
    private Map<String, Map<String, String>> userAttrs = new HashMap<>();
    private Map<String, UserInfo> userInfos = new HashMap<>();

    /**
     * Geriye sistemde tanımlı kullanıcı login isimlerini döndürür.
     *
     * @return
     */
    public List<String> getLoginNames() {

        if (loginNames == null) {
            populateLoginNames();
        }

        return loginNames;
    }

    /**
     * Kullanıcı tipine göre kullanıcı ( loginName ) listesi döndürür.
     *
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir.
     *
     * @param type
     * @return
     */
    public List<String> getUsersByType(String type) {
        List<String> ls = userByTypes.get(type);

        if (ls == null) {
            ls = populateUsersByType(type);
            userByTypes.put(type, ls);
        }

        return ls;
    }

    /**
     * Verilen Grup üyesi kullanıcı listesini döndürür.
     *
     * Grup yapısı ağaç ise o ağacın alt dallarında olan kullanıcıları da
     * döndürür.
     *
     * @param group
     * @return
     */
    public List<String> getUsersByGroup(String group) {
        List<String> ls = userByGroups.get(group);

        if (ls == null) {
            ls = populateUsersByGroup(group);
            userByGroups.put(group, ls);
        }

        return ls;
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

    
    public UserInfo getUserInfo( String loginName ){
        UserInfo ui = userInfos.get(loginName);
        
        if( ui == null ){
            ui = populateUserInfo(loginName);
            if( ui != null ){
                userInfos.put(loginName, ui);
            }
        }
        
        return ui;
    }
    
    private void populateLoginNames() {
        loginNames = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            loginNames.addAll(usp.getLoginNames());
        }
    }

    public List<String> populateUsersByType(String type) {
        List<String> ls = new ArrayList<>();

        for (UserServiceProvider usp : userServiceProviders) {
            ls.addAll(usp.getUsersByType(type));
        }

        return ls;
    }
    
    private UserInfo populateUserInfo(String loginName) {
        for (UserServiceProvider usp : userServiceProviders) {
            UserInfo ui = usp.getUserInfo(loginName);
            if( ui != null ) return ui;
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

    /**
     * İsmi verilen kullanıcının verilen listede bulunana attributelarını
     * döndürür.
     *
     * @param loginName
     * @param attributes
     * @return
     */
    public Map<String, String> populateUserAttibutes(String loginName) {
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
}
