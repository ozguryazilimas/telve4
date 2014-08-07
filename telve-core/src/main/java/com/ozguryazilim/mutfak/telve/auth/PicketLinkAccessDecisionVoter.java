/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.mutfak.telve.auth;

import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import org.apache.deltaspike.security.api.authorization.AbstractAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;
import org.picketlink.Identity;

/**
 * Sayfalara giriş yetkisi PicketLink API'leri kullanılarak belirlenir.
 * @author Hakan Uygun
 */
@ApplicationScoped
public class PicketLinkAccessDecisionVoter extends AbstractAccessDecisionVoter{

    @Inject
    @Any
    private Identity identity;
    
    @Override
    protected void checkPermission(AccessDecisionVoterContext advc, Set<SecurityViolation> set) {
        
        if( !identity.isLoggedIn() ){
            set.add(new SecurityViolation() {
                @Override
                public String getReason() {
                    return "Not Logged In;";
                }
            });
        }
    }
    
}
