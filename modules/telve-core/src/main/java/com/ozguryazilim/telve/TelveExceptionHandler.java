package com.ozguryazilim.telve;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.apache.deltaspike.core.api.exception.control.BeforeHandles;
import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.deltaspike.security.api.authorization.ErrorViewAwareAccessDeniedException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merkezi Hata yakalama ve kullanıcıyı bilgilendirme sınıfı.
 *
 * TODO: deltaspike SecurityVialotionHandler kullanımını bir düşünelim...
 *
 * @author Hakan Uygun
 */
@ExceptionHandler
@RequestScoped
public class TelveExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TelveExceptionHandler.class);

    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    private Identity userIdentity;

    void logExceptions(@BeforeHandles ExceptionEvent<Throwable> evt) {
        //ErrorViewAwareAccessDeniedException türünde olanları loglamak anlamsız. Onları login sayfasına gönderiyoruz.
        if (!(evt.getException() instanceof ErrorViewAwareAccessDeniedException)) {
            LOG.error("Something bad happened", evt.getException());
        }
    }

    /**
     * Güvenlik hatası karşısında eğer kullanıcı giriş yapmış ve parola
     * değiştirmesi gerekiyorsa ChangePassword'e diğer durumlarda login'e
     * aktarıyoruz.
     *
     * @param evt
     */
    void onSecurityExceptions(@Handles ExceptionEvent<ErrorViewAwareAccessDeniedException> evt) {
        Subject identity = SecurityUtils.getSubject();

        if (identity.isAuthenticated() && userIdentity.getUserInfo().getChangePassword()) {
            viewNavigationHandler.navigateTo(Pages.ChangePassword.class);
        } else if (identity.isAuthenticated() ) {
            //Eğer zaten login ise ve yetkisi olmayan bir yere gitmeye çalışıyor ise Ana Sayfadan başka yönlendirecek bir yerimiz yok.
            //TODO: Bir uyarı mesajı vermek fena olmaz ama dashboardda mesaj gösterilmiyor. FacesMessages.error("general.message.exception.AuthorizationException");
            viewNavigationHandler.navigateTo(Pages.Home.class);
        } else {
            viewNavigationHandler.navigateTo(Pages.Login.class);
        }
        evt.handled();
    }

}
