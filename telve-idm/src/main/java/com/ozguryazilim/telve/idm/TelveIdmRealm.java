/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.idm.entities.RolePermission;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.role.RoleRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * Telve IdM için Shiro Realm implementasyonu
 * 
 * @author Hakan Uygun
 */
public class TelveIdmRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);

        
        Set<String> roleNames = null;
        Set<String> permissions = null;

        User user = getUserRepository().findAnyByLoginName(username);
        
        // Retrieve roles and permissions from database
        roleNames = getRoleNamesForUser(user);
        //TODO: Bunu config'e almalı mı?
        //if (permissionsLookupEnabled) {
        permissions = getPermissions(user);
        //}

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        SimpleAuthenticationInfo info = null;

        String password = null;
        String salt = null;
        /* TODO: Salt Konusunda bu tür optionlar yapmalı mı?
            switch (saltStyle) {
            case NO_SALT:
                password = getPasswordForUser(conn, username)[0];
                break;
            case CRYPT:
                // TODO: separate password and hash from getPasswordForUser[0]
                throw new ConfigurationException("Not implemented yet");
                //break;
            case COLUMN:
                String[] queryResults = getPasswordForUser(conn, username);
                password = queryResults[0];
                salt = queryResults[1];
                break;
            case EXTERNAL:
                password = getPasswordForUser(conn, username)[0];
                salt = getSaltForUser(username);
            }*/

        User user = getUserRepository().findAnyByLoginName(username);
        if (user == null) {
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }

        password = user.getPasswordEncodedHash();
        salt = user.getPasswordSalt();

        if (password == null) {
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }

        info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());

        if (salt != null) {
            info.setCredentialsSalt(ByteSource.Util.bytes(salt));
        }

        return info;
    }

    //TODO: Acaba bu kodları repository'e mi taşısak?
    private Set<String> getRoleNamesForUser(User user) {
        
        Set<String> roleNames = new HashSet<>();
        
        //Böyle bir kullanıcımız yok.
        if( user == null ) return Collections.emptySet();
        
        //Kullanıcı aktif değil.
        if( !user.getActive() ) return Collections.emptySet();
        
        List<UserRole> userRoles = getUserRoleRepository().findByUser(user);
        
        
        for( UserRole ur : userRoles ){
            roleNames.add( ur.getRole().getName());
        }
        
        return roleNames;
        
    }

    //TODO: Bu kodları acaba repository'ye mi taşısak?
    private Set<String> getPermissions(User user) {
        //Böyle bir kullanıcımız yok.
        if( user == null ) return Collections.emptySet();
        
        //Kullanıcı aktif değil.
        if( !user.getActive() ) return Collections.emptySet();
        
        //Şu anda sadece user'ın rolleri üzerinden permission veriliyor.
        //Eğer grup ya da doğrudan kullanıcı üzerinden yetki verilecek ise onların da toplanması lazım.
        Set<String> permissions = new HashSet<>();
        
        List<UserRole> userRoles = getUserRoleRepository().findByUser(user);
        
        for( UserRole ur : userRoles ){
            for( RolePermission rp :  ur.getRole().getPermissions() ){
                permissions.add(rp.getPermission());
            }
        }
        
        return permissions;
    }

    
    public void clearAuthCache(PrincipalCollection principal){
        clearCachedAuthorizationInfo(principal);
    }

    public UserRepository getUserRepository() {
        return BeanProvider.getContextualReference(UserRepository.class);
    }

    public RoleRepository getRoleRepository() {
        return BeanProvider.getContextualReference(RoleRepository.class);
    }

    public UserRoleRepository getUserRoleRepository() {
        return BeanProvider.getContextualReference(UserRoleRepository.class);
    }
    
    
}
