/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.messages.FacesMessages;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author haky
 */
@RequestScoped
@Named
public class LoginController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    @Inject
    private AuditLogger auditLogger;

    @Inject
    private LoginCredentials loginCredentials;

    @Inject
    private Event<LoggedInEvent> loggedInEvent;

    public void login() {
        Boolean result = Boolean.FALSE;
        Subject currentUser = SecurityUtils.getSubject();
        try {
            if (!currentUser.isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken(loginCredentials.getUsername(), loginCredentials.getPassword());
                //this is all you have to do to support 'remember me' (no config - built in!):
                //token.setRememberMe(true);
                currentUser.login(token);
                result = currentUser.isAuthenticated();
                loggedInEvent.fire(new LoggedInEvent());
            }
        } catch (UnknownAccountException | IncorrectCredentialsException uae) {
            result = false;
            FacesMessages.error("general.message.editor.AuthFail");
        } catch (LockedAccountException | ExcessiveAttemptsException lae) {
            result = false;
            FacesMessages.error("general.message.editor.AccountLocked");
        } 

        if (result) {
            auditLogger.actionLog("Login", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, currentUser.getPrincipal().toString(), "");
        }
    }

    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        auditLogger.actionLog("Logout", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, currentUser.getPrincipal().toString(), "");
        currentUser.logout();
        facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
}
