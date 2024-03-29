package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.idm.entities.User;
import java.util.Map;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
public class TelveAdfsRealm extends TelveIdmRealm { 

    private static final Logger LOG = LoggerFactory.getLogger(TelveAdfsRealm.class);

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;

        if (!(token instanceof AdfsAuthenticationToken)) {
            throw new UnknownAccountException("Token is not a ADSF Authentication Token");
        }
         
        LOG.debug("token : {}", token );

        AdfsAuthenticationToken adsfToken = (AdfsAuthenticationToken) token;
        
        LOG.debug("AdfsAuthenticationToken : {}", adsfToken );

        String username = (String) adsfToken.getCredentials();

        LOG.debug("Authc UserName : {}", username);

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        //Eğer gelen kullanıcı sistemde yoksa otomatik olarak yeni bir kullanıcı tanımlıyoruz.
        User user = getUserRepository().findAnyByLoginName(username);
        if (user == null) {
            //Eğer otomatik oluşturma istenmiyor ise hata fırlat
            if (!getGenerateUser()) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }
            createUser(adsfToken);
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
        
        info = new SimpleAuthenticationInfo( adsfToken.getPrincipal(), "", getName());
        
        LOG.debug("adfs.getprincipal : {}", adsfToken.getPrincipal());
        
        //Login sırasında girilen veri yerine veri tabanındaki loginName kullanılsın
        username = user.getLoginName();

        info.setPrincipals(new SimplePrincipalCollection(new TelveSimplePrinciple(username), getName()));
        
        LOG.debug("getName() : {}", getName());
        
        LOG.debug("Logged UserName : {}", username);
        
        return info;

    }

    protected void createUser(AdfsAuthenticationToken adsfToken) {
        LOG.debug("User creating by TelveAdfsRealm");
        User user = new User();

        user.setLoginName(adsfToken.getAccessToken().getUsername());

        Map<String, Object> claims = adsfToken.getAccessToken().getIdTokenClaims();
        user.setFirstName((String) claims.get("name"));
        user.setLastName((String) claims.get("surname"));
        //user.setEmail(kcToken.getAccessToken().getEmail());
        user.setAutoCreated(Boolean.TRUE);

        getUserRepository().save(user);

    }

}
