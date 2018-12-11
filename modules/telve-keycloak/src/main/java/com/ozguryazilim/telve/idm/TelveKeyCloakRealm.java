/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.RolePermission;
import com.ozguryazilim.telve.idm.entities.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
public class TelveKeyCloakRealm extends TelveIdmRealm {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveKeyCloakRealm.class);
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;

        
        if( !( token instanceof KeyCloackAuthenticationToken )){
            throw new UnknownAccountException("Token is not a KeyCloak Authentication Token");
        }
        
        KeyCloackAuthenticationToken kcToken = (KeyCloackAuthenticationToken)token;
        
        String username = kcToken.getAccessToken().getPreferredUsername();
        
        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        //Eğer gelen kullanıcı sistemde yoksa otomatik olarak yeni bir kullanıcı tanımlıyoruz.
        User user = getUserRepository().findAnyByLoginName(username);
        if (user == null) {
            //FIXME: Burada kullanıcı idm veri tabanında yok ve LDAP bilgileri ile oluşturulacak.
            createUser(kcToken);
        }

        if (user != null && !user.getActive()) {
            throw new LockedAccountException("Account is lock for user [" + username + "]");
        }

        //LDAP kullanılmıyor ya da LDAP kullanıcısı olmasa da idm kullanıcı ise doğrulama yapalım.
        /* KeyCloack için böyle bir şey mümkün görünmüyor.
        if (!useLdap || (optionalLdap && info == null)) {
            password = user.getPasswordEncodedHash();

            if (password == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }

            info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());

            setCredentialsMatcher(idmMatcher);
        }*/

        info = new SimpleAuthenticationInfo(new AccessTokenPrinciple(kcToken.getAccessToken()), "", getName());
        
        return info;
        
    }
    
    
    private void createUser(KeyCloackAuthenticationToken kcToken) {
        User user = new User();

        user.setLoginName(kcToken.getAccessToken().getPreferredUsername());
        user.setFirstName(kcToken.getAccessToken().getGivenName());
        user.setLastName(kcToken.getAccessToken().getFamilyName());
        user.setEmail(kcToken.getAccessToken().getEmail());
        user.setAutoCreated(Boolean.TRUE);

        getUserRepository().save(user);
        
    }
    
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        AccessToken token = ((AccessTokenPrinciple) getAvailablePrincipal(principals)).getAccessToken();
        String username = token.getPreferredUsername();

        Set<String> roleNames = null;
        Set<String> permissions = null;

        User user = getUserRepository().findAnyByLoginName(username);

        if ("SUPERADMIN".equals(user.getUserType())) {
            permissions = new HashSet<>();
            permissions.add("*:*:*");
        } else {
            
            permissions = new HashSet<>();
            roleNames = token.getRealmAccess().getRoles();
            // Retrieve roles and permissions from database
            //roleNames = getRoleNamesForUser(user);
            //permissions = getPermissions(user);
            

            for( String rn : roleNames){
                Role r = getRoleRepository().findAnyByName(rn);
                //Telve'de tanımlı olmayan bir role gelirse kaale almıyoruz
                if( r != null ){
                    for( RolePermission rp : r.getPermissions()){
                        permissions.add( rp.getPermission());
                    }
                }
            }
            
            //$owner ve $group ile ilgili yetki zenginleştirmesi
            String groupScope = "";
            Set<String> scopePerms = new HashSet<>();
            for( String p : permissions ){
                if( p.contains("$group")){
                    //Eğer daha önceden grup üyeleri bulunmadıysa onları bir bulalım
                    if( Strings.isNullOrEmpty(groupScope) ){
                        List<String> ls = getUserRepository().findAllGroupMembers( username );
                        groupScope = Joiner.on(',').join(ls).toString();
                    }
                    
                    scopePerms.add(p.replace("$group", groupScope));
                    
                } else if( p.contains("$owner")){
                    scopePerms.add(p.replace("$owner", username));
                }
            }
            
            LOG.debug("Scope Permissions {}", scopePerms);
            if( !scopePerms.isEmpty()){
                permissions.addAll(scopePerms);
            }
            
        }

        //SSO'dan gelen kimse burada parola değiştiremez
        //Normal kullanıcılar ( LDAP'tan vs gelmeyen ) kendi parolasını değiştirebilmeli.
        //if (!user.getAutoCreated()) {
        //    permissions.add("PasswordEditor:*");
        //}

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }
}
