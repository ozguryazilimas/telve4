package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.idm.helpers.IdentityContextData;
import org.apache.shiro.authc.AuthenticationToken;

/**
 *
 * @author oyas
 */
public class AdfsAuthenticationToken implements AuthenticationToken{

    private IdentityContextData context;

    public AdfsAuthenticationToken(IdentityContextData context) {
        this.context = context;
    }

    public IdentityContextData getAccessToken() {
        return context;
    }
    
    @Override
    public Object getPrincipal() {
        return new AdsfAccessTokenPrinciple(context);
    }

    @Override
    public Object getCredentials() {
        return context.getUsername();
    }
    
}
