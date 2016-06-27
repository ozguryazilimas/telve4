/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.admin;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.basic.Role;

/**
 * Role JSF Converter.
 * 
 * @author Hakan Uygun
 */
@FacesConverter("roleConverter")
@RequestScoped
public class RoleConverter implements Converter{

    @Inject
    private IdentityManager identityManager;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Role> ls =identityManager.createIdentityQuery(Role.class).setParameter(Role.ID, value).getResultList();
        if( !ls.isEmpty()){
            return ls.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Role r = (Role)value;
        return r.getId();
    }
    
}
