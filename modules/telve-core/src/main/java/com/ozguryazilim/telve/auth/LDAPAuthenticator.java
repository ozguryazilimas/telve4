/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LDAP üzerinden kullanıcı ad/parola kontrolü yapar.
 * 
 * LDAP Bağlantı bilgileri için properties dosyasından şu değerleri okur :
 * 
 * auth.ldap.url=ldap://localhost:10389
 * auth.ldap.bindDN=uid=admin,ou=system
 * auth.ldap.bindPS=secret
 * auth.ldap.baseDN=ou=People,dc=example,dc=com
 * auth.ldap.userQuery=(uid=:username)
 * 
 * @author Hakan Uygun
 */
@Named
@RequestScoped
public class LDAPAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(LDAPAuthenticator.class);

    /*
    @Inject
    private DefaultLoginCredentials credentials;

    @Inject
    private IdentityManager identityManager;

    @Override
    public void authenticate() {

        LOG.debug("LDAP Auth");

        try {

            Credentials.Status status = authenticateJndi(credentials.getUserId(), credentials.getPassword());

            if (Credentials.Status.VALID.equals(status)) {
                LOG.debug("LDAP Auth : OK");
                this.credentials.setStatus(Credentials.Status.VALID);

                //LDAP'dan doğrulandı şimdi diğer bilgileri PL'den alıyoruz.
                User u = BasicModel.getUser(identityManager, credentials.getUserId());
                if (u != null) {
                    this.credentials.setValidatedAccount(u);
                } else {
                    this.credentials.setStatus(Credentials.Status.INVALID);
                }
            } else if (Credentials.Status.UNVALIDATED.equals(status)) {
                //LDAP'da bu kullanıcı yok PL'den doğrulama yoluna gidelim.
                Credentials creds;

                creds = new UsernamePasswordCredentials(credentials.getUserId(),
                        (Password) credentials.getCredential());

                identityManager.validateCredentials(creds);

                this.credentials.setStatus(creds.getStatus());
                this.credentials.setValidatedAccount(creds.getValidatedAccount());
            } else {
                //LDAP'da kullanıcı var ama bilgiler hatalı
                this.credentials.setStatus(Credentials.Status.INVALID);
            }
        } catch (Exception ex) {
            LOG.error("LDAP Error", ex);
            this.credentials.setStatus(Credentials.Status.INVALID);
        }

        if (Credentials.Status.VALID.equals(credentials.getStatus())) {
            setStatus(AuthenticationStatus.SUCCESS);
            setAccount(credentials.getValidatedAccount());
        } else if (Credentials.Status.ACCOUNT_DISABLED.equals(credentials.getStatus())) {
            throw new LockedAccountException("Account [" + this.credentials.getUserId() + "] is disabled.");
        } else if (Credentials.Status.EXPIRED.equals(credentials.getStatus())) {
            throw new CredentialExpiredException("Credential is expired for Account [" + this.credentials.getUserId() + "].");
        }

    }

    public Credentials.Status authenticateJndi(String username, String password) throws Exception {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, ConfigResolver.getPropertyValue("auth.ldap.url", "ldap://localhost:10389"));
        props.put(Context.SECURITY_PRINCIPAL, ConfigResolver.getPropertyValue("auth.ldap.bindDN", "uid=admin,ou=system"));//adminuser - User with special priviledge, dn user
        props.put(Context.SECURITY_CREDENTIALS, ConfigResolver.getPropertyValue("auth.ldap.bindPS", "secret"));//dn user password

        InitialDirContext context = new InitialDirContext(props);

        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[]{"givenName", "sn", "memberOf"});
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String qs = ConfigResolver.getPropertyValue("auth.ldap.userQuery", "(uid=:username)");
        qs = qs.replace(":username", username);

        NamingEnumeration<javax.naming.directory.SearchResult> answers = context.search(ConfigResolver.getPropertyValue("auth.ldap.baseDN", "ou=People,dc=example,dc=com"), qs, ctrls);
        
        
        if( !answers.hasMoreElements() ){
            //Kullanıcı LDAP'ta tanımlı değil.
            return Credentials.Status.UNVALIDATED;
        }
        
        javax.naming.directory.SearchResult result = answers.nextElement();
        String user = result.getNameInNamespace();

        try {
            props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put(Context.PROVIDER_URL, ConfigResolver.getPropertyValue("auth.ldap.url", "ldap://localhost:10389"));
            props.put(Context.SECURITY_PRINCIPAL, user);
            props.put(Context.SECURITY_CREDENTIALS, password);

            context = new InitialDirContext(props);
        } catch (Exception e) {
            return Credentials.Status.INVALID;
        }
        return Credentials.Status.VALID;
    }
*/
}
