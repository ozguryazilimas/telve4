/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.authentication.UserAlreadyLoggedInException;
import org.picketlink.idm.model.basic.User;

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
    
    @Inject
    private AuditLogger auditLogger;
    
    public void login() {
        AuthenticationResult result = AuthenticationResult.FAILED;
        try{
            result = identity.login();
        } catch ( UserAlreadyLoggedInException ex ){
            //Zaten login olduğu için hata vermek biraz saçma :) 
            //Aslında buraya da gelmemesi gerektiği için doğrudan home'a yönlendiriyoruz.
            result = AuthenticationResult.SUCCESS;
            this.viewNavigationHandler.navigateTo(Pages.Home.class);
            return;
        }
        
        if (AuthenticationResult.FAILED.equals(result)) {
            FacesMessages.error("general.message.editor.AuthFail");
        } else {
            auditLogger.actionLog("Login", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, ((User)identity.getAccount()).getLoginName(), "" );
        } 
    }
    
    public String logout(){
        auditLogger.actionLog("Logout", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, ((User)identity.getAccount()).getLoginName(), "" );
        identity.logout();
        facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
}
