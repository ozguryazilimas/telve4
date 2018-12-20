package com.ozguryazilim.telve.auth;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * LDAP/IDM Authenticator seçimi sağlar.
 * 
 * Kontrol için properties dosyasında auth.method=IDM|LDAP değerine bakar.
 * 
 * @author Hakan Uygun
 */
@RequestScoped
@Named
public class AuthenticatorSelector {
    
//    @Inject
//    private Instance<LDAPAuthenticator> ldapAuthenticator;
//
//    @Inject 
//    private Instance<IdmAuthenticator> idmAuthenticator;
//    
//    @Produces
//    @PicketLink
//    @Named
//    @RequestScoped
//    public Authenticator selectAuthenticator() {
//        String authenticator = ConfigResolver.getPropertyValue("auth.method","IDM");
//        if ("IDM".equals(authenticator)) {
//            return idmAuthenticator.get();
//        } else {
//            return ldapAuthenticator.get();
//        }
//
//    }
}
