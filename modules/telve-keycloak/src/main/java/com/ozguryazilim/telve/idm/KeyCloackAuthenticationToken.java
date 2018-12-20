package com.ozguryazilim.telve.idm;

import org.apache.shiro.authc.AuthenticationToken;
import org.keycloak.representations.AccessToken;

/**
 *
 * @author oyas
 */
public class KeyCloackAuthenticationToken implements AuthenticationToken{

    private AccessToken accessToken;

    public KeyCloackAuthenticationToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }
    
    
    
    @Override
    public Object getPrincipal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
