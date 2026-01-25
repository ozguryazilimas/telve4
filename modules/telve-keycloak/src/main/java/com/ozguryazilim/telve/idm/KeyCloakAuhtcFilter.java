package com.ozguryazilim.telve.idm;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
public class KeyCloakAuhtcFilter extends AuthenticatingFilter{

    private static final Logger LOG = LoggerFactory.getLogger(KeyCloakAuhtcFilter.class);
    
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        LOG.debug("Keycloak Authentication Token created");
        return new KeyCloackAuthenticationToken( getSession((HttpServletRequest)request).getToken());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        LOG.debug("Shiro login by Keycloak Authentication Token");
        return executeLogin(request, response);
    }
    
    
    private KeycloakSecurityContext getSession(HttpServletRequest req) {
        return (KeycloakSecurityContext) req.getAttribute(KeycloakSecurityContext.class.getName());
}
}
