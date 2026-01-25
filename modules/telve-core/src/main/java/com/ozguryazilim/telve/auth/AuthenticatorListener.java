package com.ozguryazilim.telve.auth;

import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.util.Map;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameterContext;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;


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
    private TelveAccessDecisionVoter accessDecisionVoter;
    
    @Inject
    private NavigationParameterContext navigationParameterContext;
    
    @Inject
    private Identity identity;
    
    /**
     * Login olunduktan sonra bir şekilde hata nedeniyle logine gelmiş isek o sayfaya geri dönelim...
     * @param event 
     */
    public void handleLoggedIn(@Observes LoggedInEvent event) {
        //Geri dönülecek sayfa için request parametreleri varsa koyalım
        Map<String, String> m = accessDecisionVoter.getRequestParams();
        if (m.containsKey("jakarta.faces.partial.ajax")) {
            //Ajax sorgusu. Dolayısı ile büyük ihtimal çağrıldığı yerle ilgili şeyler kayıp.
            //O yüzden ana sayfaya gidiyoruz.
            this.viewNavigationHandler.navigateTo(Pages.Home.class);
        } else {
            for (Map.Entry<String, String> e : m.entrySet()) {
                navigationParameterContext.addPageParameter(e.getKey(), e.getValue());
            }
            //Login olduktan sonra getPasswordChange() true ise parola değiştirme sayfasına gönderiyoruz.
            if (identity.getUserInfo().getManaged() && identity.getUserInfo().getChangePassword()) {
                this.viewNavigationHandler.navigateTo(Pages.ChangePassword.class);
            } else {
                this.viewNavigationHandler.navigateTo(accessDecisionVoter.getDeniedPage());
            }
        }
    }
    //PasswordChange eventini dinler ve sorun yoksa 
    public void handlePasswordChange(@Observes PasswordChangeEvent event) {
        //Geri dönülecek sayfa için request parametreleri varsa koyalım
        Map<String, String> m = accessDecisionVoter.getRequestParams();
        if (m.containsKey("jakarta.faces.partial.ajax")) {
            //Ajax sorgusu. Dolayısı ile büyük ihtimal çağrıldığı yerle ilgili şeyler kayıp.
            //O yüzden ana sayfaya gidiyoruz.
            this.viewNavigationHandler.navigateTo(Pages.Home.class);
        } else {
            for (Map.Entry<String, String> e : m.entrySet()) {
                navigationParameterContext.addPageParameter(e.getKey(), e.getValue());
            }
            //Parolayı değiştirdikten sonra Doğru sayfaya yönlendiriyoruz.
            this.viewNavigationHandler.navigateTo(accessDecisionVoter.getDeniedPage());
        }
    }
}
