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
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.role.RoleRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Telve IdM için Shiro Realm implementasyonu
 *
 *
 * JndiLdap üzerinden LDAP kullanıcı doğrulaması da yapar.
 *
 * bu işlemler için şu seçenekler kullanılır :
 *
 * LDAP doğrulaması kullanacak mıyız? LDAP kullanıcısı olmaması halinde veri
 * tabanına bakacak mıyız? LDAP kullanıcısını otomatik oluşturacak mıyız?
 *
 * @author Hakan Uygun
 */
public class TelveIdmRealm extends JndiLdapRealm {

    private static final Logger LOG = LoggerFactory.getLogger(TelveIdmRealm.class);

    /**
     * LDAP doğrulaması kullanacak mıyız?
     */
    private Boolean useLdap = Boolean.FALSE;
    /**
     * LDAP kullanıcısı olmaması halinde veri tabanına bakacak mıyız?
     */
    private Boolean optionalLdap = Boolean.TRUE;
    /**
     * LDAP kullanıcısını otomatik oluşturacak mıyız?
     */
    private Boolean generateUser = Boolean.FALSE;

    private String groupSearchBase;
    private String userSearchBase;
    private String userSearchFilter = "(uid={0})";

    private String firstNameAttr = "givenName";
    private String lastNameAttr = "sn";
    private String emailAttr = "mail";
    private String loginNameAttr = "uid";
    private String groupNameAttr = "cn";
    private String groupMembersAttr = "memberUid";

    private String defaultRole = "";

    private CredentialsMatcher ldapMatcher = new AllowAllCredentialsMatcher();
    private CredentialsMatcher idmMatcher = new PasswordMatcher();

    public Boolean getUseLdap() {
        return useLdap;
    }

    public void setUseLdap(Boolean useLdap) {
        this.useLdap = useLdap;
    }

    public Boolean getOptionalLdap() {
        return optionalLdap;
    }

    public void setOptionalLdap(Boolean optionalLdap) {
        this.optionalLdap = optionalLdap;
    }

    public Boolean getGenerateUser() {
        return generateUser;
    }

    public void setGenerateUser(Boolean generateUser) {
        this.generateUser = generateUser;
    }

    public String getGroupSearchBase() {
        return groupSearchBase;
    }

    public void setGroupSearchBase(String groupSearchBase) {
        this.groupSearchBase = groupSearchBase;
    }

    public String getUserSearchBase() {
        return userSearchBase;
    }

    public void setUserSearchBase(String userSearchBase) {
        this.userSearchBase = userSearchBase;
    }

    public String getUserSearchFilter() {
        return userSearchFilter;
    }

    public void setUserSearchFilter(String userSearchFilter) {
        this.userSearchFilter = userSearchFilter;
    }

    public String getFirstNameAttr() {
        return firstNameAttr;
    }

    public void setFirstNameAttr(String firstNameAttr) {
        this.firstNameAttr = firstNameAttr;
    }

    public String getLastNameAttr() {
        return lastNameAttr;
    }

    public void setLastNameAttr(String lastNameAttr) {
        this.lastNameAttr = lastNameAttr;
    }

    public String getEmailAttr() {
        return emailAttr;
    }

    public void setEmailAttr(String emailAttr) {
        this.emailAttr = emailAttr;
    }

    public String getLoginNameAttr() {
        return loginNameAttr;
    }

    public void setLoginNameAttr(String loginNameAttr) {
        this.loginNameAttr = loginNameAttr;
    }

    public String getGroupNameAttr() {
        return groupNameAttr;
    }

    public void setGroupNameAttr(String groupNameAttr) {
        this.groupNameAttr = groupNameAttr;
    }

    public String getGroupMembersAttr() {
        return groupMembersAttr;
    }

    public void setGroupMembersAttr(String groupMembersAttr) {
        this.groupMembersAttr = groupMembersAttr;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = ((TelveSimplePrinciple) getAvailablePrincipal(principals)).getName();

        Set<String> roleNames = null;
        Set<String> permissions = null;

        User user = getUserRepository().findAnyByLoginName(username);

        if ("SUPERADMIN".equals(user.getUserType())) {
            permissions = new HashSet<>();
            permissions.add("*:*:*");
        } else {
            // Retrieve roles and permissions from database
            roleNames = getRoleNamesForUser(user);
            permissions = getPermissions(user);
            

            //$owner ve $group ile ilgili yetki zenginleştirmesi
            String groupScope = "";
            Set<String> scopePerms = new HashSet<>();
            for( String p : permissions ){
                if( p.contains("$group")){
                    //Eğer daha önceden grup üyeleri bulunmadıysa onları bir bulalım
                    if( Strings.isNullOrEmpty(groupScope) ){
                        List<String> ls = getUserRepository().findAllGroupMembers( username );
                        groupScope = Joiner.on(',').join(ls);
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

        //Normal kullanıcılar ( LDAP'tan vs gelmeyen ) kendi parolasını değiştirebilmeli.
        if (!user.getAutoCreated()) {
            permissions.add("PasswordEditor:*");
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        SimpleAuthenticationInfo info = null;
        if (useLdap) {
            try {
                setCredentialsMatcher(ldapMatcher);
                info = (SimpleAuthenticationInfo) queryForAuthenticationInfo(token, getContextFactory());
            } catch (AuthenticationNotSupportedException e) {
                String msg = "Unsupported configured authentication mechanism";
                throw new UnsupportedAuthenticationMechanismException(msg, e);
            } catch (javax.naming.AuthenticationException e) {
                if (!optionalLdap) {
                    throw new AuthenticationException("LDAP authentication failed.", e);
                }
            } catch (NamingException e) {
                String msg = "LDAP naming error while attempting to authenticate user.";
                throw new AuthenticationException(msg, e);
            }
        }

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        String password = null;

        User user = getUserRepository().findAnyByLoginName(username);
        if (user == null) {
            if (!useLdap || info == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            } else {
                //Eğer otomatik oluşturma istenmiyor ise hata fırlat
                if (!generateUser) {
                    throw new UnknownAccountException("No account found for user [" + username + "]");
                }

                try {
                    //FIXME: Burada kullanıcı idm veri tabanında yok ve LDAP bilgileri ile oluşturulacak.
                    createUser(upToken);
                } catch (NamingException ex) {
                    throw new UnknownAccountException("No account created for user [" + username + "]");
                }
            }
        } else {
            if (!user.getActive()) {
                throw new LockedAccountException("Account is lock for user [" + username + "]");
            }
            
            //Login sırasında girilen veri yerine veri tabanındaki loginName kullanılsın
            upToken.setUsername(user.getLoginName());
            username = user.getLoginName();
            
            if( info != null ){
                info.setPrincipals(new SimplePrincipalCollection(new TelveSimplePrinciple(username), getName()));
            }
        }

        //LDAP kullanılmıyor ya da LDAP kullanıcısı olmasa da idm kullanıcı ise doğrulama yapalım.
        if (!useLdap || (optionalLdap && info == null)) {
            password = user.getPasswordEncodedHash();

            if (password == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }

            info = new SimpleAuthenticationInfo( new TelveSimplePrinciple(username), password.toCharArray(), getName());

            setCredentialsMatcher(idmMatcher);
        }

        return info;
    }

    //TODO: Acaba bu kodları repository'e mi taşısak?
    private Set<String> getRoleNamesForUser(User user) {

        Set<String> roleNames = new HashSet<>();

        //Böyle bir kullanıcımız yok.
        if (user == null) {
            return Collections.emptySet();
        }

        //Kullanıcı aktif değil.
        if (!user.getActive()) {
            return Collections.emptySet();
        }

        List<UserRole> userRoles = getUserRoleRepository().findByUserAndRole_active( user, true );

        for (UserRole ur : userRoles) {
            roleNames.add(ur.getRole().getName());
        }

        return roleNames;

    }

    //TODO: Bu kodları acaba repository'ye mi taşısak?
    private Set<String> getPermissions(User user) {
        //Böyle bir kullanıcımız yok.
        if (user == null) {
            return Collections.emptySet();
        }

        //Kullanıcı aktif değil.
        if (!user.getActive()) {
            return Collections.emptySet();
        }

        //Şu anda sadece user'ın rolleri üzerinden permission veriliyor.
        //Eğer grup ya da doğrudan kullanıcı üzerinden yetki verilecek ise onların da toplanması lazım.
        Set<String> permissions = new HashSet<>();

        List<UserRole> userRoles = getUserRoleRepository().findByUserAndRole_active(user, true);

        for (UserRole ur : userRoles) {
            for (RolePermission rp : ur.getRole().getPermissions()) {
                permissions.add(rp.getPermission());
            }
        }

        return permissions;
    }

    public void clearAuthCache(PrincipalCollection principal) {
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

    /**
     * LDAP'tan doğrulaması yapıldı. Dolayısı ile böyle bir kullanıcı var. Hadi
     * onu default bir role ile oluşturalım.
     *
     * FirstName, LastName ve Email bilgileri LDAP'tan alınacak.
     *
     * @param upToken
     */
    protected void createUser(UsernamePasswordToken upToken) throws NamingException {

        LdapContext ldapContext = getContextFactory().getSystemLdapContext();

        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[]{getFirstNameAttr(), getLastNameAttr(), getEmailAttr()});
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        Object[] searchArguments = new Object[]{upToken.getUsername()};

        NamingEnumeration<SearchResult> answers = ldapContext.search(getUserSearchBase(), getUserSearchFilter(), searchArguments, ctrls);

        if (!answers.hasMoreElements()) {
            //Kullanıcı LDAP'ta tanımlı değil.

        }

        SearchResult result = answers.nextElement();
        String firstName = result.getAttributes().get(getFirstNameAttr()) != null ? (String) result.getAttributes().get(getFirstNameAttr()).get() : null;
        String lastName = result.getAttributes().get(getLastNameAttr()) != null ? (String) result.getAttributes().get(getLastNameAttr()).get() : null;
        String email = result.getAttributes().get(getEmailAttr()) != null ? (String) result.getAttributes().get(getEmailAttr()).get() : null;

        User user = new User();

        user.setLoginName(upToken.getUsername());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAutoCreated(Boolean.TRUE);

        getUserRepository().save(user);

        //Varsayılan bir role varsa onun da atamasını yapalım.
        if (!Strings.isNullOrEmpty(getDefaultRole())) {

            Role r = getRoleRepository().findAnyByName(getDefaultRole());

            if (r != null) {
                UserRole ur = new UserRole();
                ur.setUser(user);
                ur.setRole(r);

                getUserRoleRepository().save(ur);
            }
        }

        LOG.debug("User {} is created with role {}", upToken.getUsername(), getDefaultRole());
    }

    @Override
    protected AuthenticationInfo createAuthenticationInfo(AuthenticationToken token, Object ldapPrincipal, Object ldapCredentials, LdapContext ldapContext) throws NamingException {
        return new SimpleAuthenticationInfo(new TelveSimplePrinciple(token.getPrincipal().toString()), token.getCredentials(), getName());
    }

    
}
