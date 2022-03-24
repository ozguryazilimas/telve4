package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.idm.helpers.AuthHelper;
import com.ozguryazilim.telve.idm.helpers.IdentityContextAdapter;
import com.ozguryazilim.telve.idm.helpers.IdentityContextAdapterServlet;
import com.ozguryazilim.telve.idm.helpers.IdentityContextData;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
public class AdfsAuhtcFilter extends AuthenticatingFilter{

    private static final Logger LOG = LoggerFactory.getLogger(AdfsAuhtcFilter.class);
    
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        LOG.debug("ADFS Authentication Token created");
        IdentityContextAdapter contextAdapter = new IdentityContextAdapterServlet((HttpServletRequest) request, (HttpServletResponse) response);
        IdentityContextData context = contextAdapter.getContext();
        
        if( context == null || !context.getAuthenticated()){
            AuthHelper.signIn(contextAdapter);
        }
        
        LOG.debug("Claims: {}", context.getIdTokenClaims());
        
        return new AdfsAuthenticationToken( context );
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        LOG.debug("Shiro login by Keycloak Authentication Token");
        return executeLogin(request, response);
    }

    @Override
    public String getLoginUrl() {
        //Redirect URL'ini login ural olarak tanımlayalım ki filtre çalışmasın
        return "/auth/redirect";
    }

    
    
    
    
}
