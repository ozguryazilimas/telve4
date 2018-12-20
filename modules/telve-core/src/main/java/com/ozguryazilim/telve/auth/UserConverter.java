package com.ozguryazilim.telve.auth;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


/**
 * User JSF Converter.
 * 
 * @author Hakan Uygun
 */
@FacesConverter("userConverter")
@RequestScoped
public class UserConverter implements Converter{

//    @Inject
//    private IdentityManager identityManager;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        //return identityManager.lookupIdentityById(User.class, value);
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        //return ((User)value).getId();
        return "";
    }
    
}
