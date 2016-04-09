/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;
import org.picketlink.authentication.internal.IdmAuthenticator;

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
    
    @Inject
    private Instance<LDAPAuthenticator> ldapAuthenticator;

    @Inject 
    private Instance<IdmAuthenticator> idmAuthenticator;
    
    @Produces
    @PicketLink
    @Named
    @RequestScoped
    public Authenticator selectAuthenticator() {
        String authenticator = ConfigResolver.getPropertyValue("auth.method","IDM");
        if ("IDM".equals(authenticator)) {
            return idmAuthenticator.get();
        } else {
            return ldapAuthenticator.get();
        }

    }
}
