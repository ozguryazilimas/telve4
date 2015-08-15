/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.view.Pages;
import java.util.Set;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.security.api.authorization.AbstractAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;
import org.picketlink.Identity;

/**
 * Sayfalara giriş yetkisi PicketLink API'leri kullanılarak belirlenir.
 *
 * @author Hakan Uygun
 */
@WindowScoped
public class PicketLinkAccessDecisionVoter extends AbstractAccessDecisionVoter {

    private Class<? extends ViewConfig> deniedPage = Pages.Home.class;

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    @Any
    private Identity identity;

    @Override
    protected void checkPermission(AccessDecisionVoterContext advc, Set<SecurityViolation> set) {

        if (!identity.isLoggedIn()) {
            set.add(new SecurityViolation() {
                @Override
                public String getReason() {
                    return "Not Logged In;";
                }
            });
            deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
        }

        SecuredPage sc = advc.getMetaDataFor(SecuredPage.class.getName(), SecuredPage.class);
        if (!Strings.isNullOrEmpty(sc.value())) {
            if (!identity.hasPermission(sc.value(), "select")) {
                set.add(new SecurityViolation() {
                    @Override
                    public String getReason() {
                        return "Not have permission;";
                    }
                });

                deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
            }
        }

    }

    public Class<? extends ViewConfig> getDeniedPage() {
        try {
            return deniedPage;
        } finally {
            deniedPage = Pages.Home.class;
        }
    }

}
