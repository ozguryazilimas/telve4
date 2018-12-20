package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.TelveIdmPrinciple;
import java.io.Serializable;
import org.keycloak.representations.AccessToken;

/**
 *
 * @author oyas
 */
public class AccessTokenPrinciple implements TelveIdmPrinciple, Serializable{

    private final AccessToken accessToken;

    public AccessTokenPrinciple(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
    
    @Override
    public String getName() {
        return accessToken.getPreferredUsername();
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }
    
}
