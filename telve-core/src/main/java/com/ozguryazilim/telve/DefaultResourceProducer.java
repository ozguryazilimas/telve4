/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.data.api.audit.CurrentUser;
import org.picketlink.Identity;

/**
 * Telve uygulamaları içerisinde kullanılacak temel kaynakları üretir.
 *
 * @author Hakan Uygun
 */
@RequestScoped
public class DefaultResourceProducer {

    @Inject @Any
    private Identity identity;
    
    @Produces @Default
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    @Produces @CurrentUser
    public String currentUser() {
        return identity.getAccount().getId();
    }
    
}
