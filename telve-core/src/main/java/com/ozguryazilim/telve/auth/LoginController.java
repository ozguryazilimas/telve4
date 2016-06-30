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
        //try{
            //FIXME: Default kullanıcı için bir şey düşünelim.
            //SecurityUtils.getSecurityManager();
            
            Subject currentUser = SecurityUtils.getSubject();
            if( !currentUser.isAuthenticated()){
                UsernamePasswordToken token = new UsernamePasswordToken(loginCredentials.getUsername(), loginCredentials.getPassword());
                //this is all you have to do to support 'remember me' (no config - built in!):
                //token.setRememberMe(true);
                currentUser.login(token);
                result = currentUser.isAuthenticated();
                loggedInEvent.fire(new LoggedInEvent());
            }
            
            
//        } catch ( UserAlreadyLoggedInException ex ){
//            //Zaten login olduğu için hata vermek biraz saçma :) 
//            //Aslında buraya da gelmemesi gerektiği için doğrudan home'a yönlendiriyoruz.
//            result = AuthenticationResult.SUCCESS;
//            this.viewNavigationHandler.navigateTo(Pages.Home.class);
//            return;
//        }
        
        if (!result) {
            FacesMessages.error("general.message.editor.AuthFail");
        } else {
            auditLogger.actionLog("Login", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, currentUser.getPrincipal().toString(), "" );
        } 
    }
    
    public String logout(){
        Subject currentUser = SecurityUtils.getSubject();
        auditLogger.actionLog("Logout", 0l, "", AuditLogCommand.CAT_AUTH, AuditLogCommand.ACT_AUTH, currentUser.getPrincipal().toString(), "" );
        currentUser.logout();
        facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
}
