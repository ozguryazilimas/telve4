/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.ozguryazilim.telve.messages.FacesMessages;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

/**
 *
 * @author haky
 */
@RequestScoped
@Named
public class LoginController {

    @Inject
    private Identity identity;
    @Inject
    private FacesContext facesContext;

    @Inject
    private ViewNavigationHandler viewNavigationHandler;
    
    
    public void login() {
        AuthenticationResult result = identity.login();
        if (AuthenticationResult.FAILED.equals(result)) {
            FacesMessages.error("general.message.editor.AuthFail");
        } 
    }
    
    public String logout(){
        identity.logout();
        facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
}
