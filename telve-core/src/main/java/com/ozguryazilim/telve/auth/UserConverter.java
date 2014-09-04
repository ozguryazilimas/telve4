/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.basic.User;

/**
 * User JSF Converter.
 * 
 * @author Hakan Uygun
 */
@FacesConverter("userConverter")
@RequestScoped
public class UserConverter implements Converter{

    @Inject
    private IdentityManager identityManager;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return identityManager.lookupIdentityById(User.class, value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((User)value).getId();
    }
    
}
