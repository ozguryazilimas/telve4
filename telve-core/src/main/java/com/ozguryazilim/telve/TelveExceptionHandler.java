/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve;

import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.apache.deltaspike.core.api.exception.control.BeforeHandles;
import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.deltaspike.security.api.authorization.ErrorViewAwareAccessDeniedException;

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

    @Inject
    private ViewNavigationHandler viewNavigationHandler;
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private FacesContext facesContext;
    
    void logExceptions(@BeforeHandles ExceptionEvent<Throwable> evt) {
        //log.warning("Something bad happened: " + evt.getException().getMessage());
    }

    /**
     * Güvenlik hatası karşısında login'e gönderiyoruz.
     * 
     * @param evt 
     */
    void onSecurityExceptions(@Handles ExceptionEvent<ErrorViewAwareAccessDeniedException> evt) {
        
        viewNavigationHandler.navigateTo(Pages.Login.class);
        
        evt.handled();
    }

}
