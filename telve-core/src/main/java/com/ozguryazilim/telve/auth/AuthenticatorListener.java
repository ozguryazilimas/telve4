/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.picketlink.authentication.event.LoggedInEvent;

/**
 * Login Event dinler ve kullanıcıyı doğru sayfaya yönlendirir.
 * 
 * @author Hakan Uygun
 */
@SessionScoped
public class AuthenticatorListener implements Serializable{
   
    @Inject
    private ViewNavigationHandler viewNavigationHandler;
    
    @Inject
    private PicketLinkAccessDecisionVoter accessDecisionVoter;
    
    public void handleLoggedIn(@Observes LoggedInEvent event) {
        this.viewNavigationHandler.navigateTo(accessDecisionVoter.getDeniedPage());
    }
}
