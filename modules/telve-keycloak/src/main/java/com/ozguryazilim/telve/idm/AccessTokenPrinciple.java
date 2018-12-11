/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
