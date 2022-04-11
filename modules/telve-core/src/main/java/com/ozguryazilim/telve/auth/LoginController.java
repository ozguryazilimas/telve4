package com.ozguryazilim.telve.auth;

import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
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
public class LoginController{

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
    
    @Inject
    private Identity identity;
    
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
                //Kullanıcı sorunsuz login oldu ve artık parola değiştirmesi gerekli mi diye kontrol etmeliyiz.
                loggedInEvent.fire(new LoggedInEvent(identity.getUserInfo().getChangePassword()));
            } else {
                //Yani zaten login olmuş durumda ise Ana sayfaya yollayalım
                 this.viewNavigationHandler.navigateTo(Pages.Home.class);
            }
        } catch (UnknownAccountException | IncorrectCredentialsException uae) {
            result = false;
            auditLogger.actionLog("Login attempts", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, loginCredentials.getUsername(), "Auth Fail");
            FacesMessages.error("general.message.AuthFail");
        } catch (LockedAccountException | ExcessiveAttemptsException lae) {
            result = false;
            auditLogger.actionLog("Login attempts", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, loginCredentials.getUsername(), "Account Locked");
            FacesMessages.error("general.message.AccountLocked");
        } 

        if (result) {
            auditLogger.actionLog("Login", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, identity.getLoginName(), "");
        }
    }

    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        auditLogger.actionLog("Logout", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, identity.getLoginName(), "");
        currentUser.logout();
        facesContext.getExternalContext().invalidateSession();
        String redirectTo = ConfigResolver.getPropertyValue("logout.redirect", "/home.xhtml?faces-redirect=true");
        return redirectTo;
    }
}
