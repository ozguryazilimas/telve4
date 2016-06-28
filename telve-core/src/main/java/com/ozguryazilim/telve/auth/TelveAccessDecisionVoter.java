/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.view.Pages;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.security.api.authorization.AbstractAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sayfalara giriş yetkisi Shiro API'leri kullanılarak belirlenir.
 *
 * @author Hakan Uygun
 */
@WindowScoped
public class TelveAccessDecisionVoter extends AbstractAccessDecisionVoter {

    private static final Logger LOG = LoggerFactory.getLogger(TelveAccessDecisionVoter.class);

    private Class<? extends ViewConfig> deniedPage = Pages.Home.class;
    private Map<String, String> requestParams = new HashMap<>();

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Override
    protected void checkPermission(AccessDecisionVoterContext advc, Set<SecurityViolation> set) {

        try {
            Subject identity = SecurityUtils.getSubject();

            if (!identity.isAuthenticated()) {
                set.add(new SecurityViolation() {
                    @Override
                    public String getReason() {
                        return "Not Logged In;";
                    }
                });
                //Hatayı ürettiğimiz yeri hatırlayalım. Ki login sonrası geri dönebilelim.
                requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
            }

            SecuredPage sc = advc.getMetaDataFor(SecuredPage.class.getName(), SecuredPage.class);
            if (!Strings.isNullOrEmpty(sc.value())) {
                if (!identity.isPermitted(sc.value() + ":select")) {
                    set.add(new SecurityViolation() {
                        @Override
                        public String getReason() {
                            return "Not have permission;";
                        }
                    });
                    //Hatayı ürettiğimiz yeri hatırlayalım. Belki farklı yetkili biri olarak geri gelir :)
                    requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                    deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
                }

                //Uygulama için ayarlardan yetki çıkarma kontrolü
                if ("true".equals(ConfigResolver.getPropertyValue("permission.exclude." + sc.value(), "false"))) {
                    set.add(new SecurityViolation() {
                        @Override
                        public String getReason() {
                            return "Not have permission;";
                        }
                    });
                    //Buraya bir daha hiç gelemeyecekler
                    //deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
                }
            }
        } catch (Exception e) {
            //Her ne türden exception olursa olsun izin vermiyoruz.
            LOG.error("Security Control Error", e);
            set.add(new SecurityViolation() {
                @Override
                public String getReason() {
                    return "Error on check permission;";
                }
            });
        }

    }

    /**
     * Security exception verildiğinde bulunulan sayfa
     *
     * @return
     */
    public Class<? extends ViewConfig> getDeniedPage() {
        try {
            return deniedPage;
        } finally {
            deniedPage = Pages.Home.class;
        }
    }

    /**
     * Security exception verildiğinde sahip olunan request parametreleri
     *
     * @return
     */
    public Map<String, String> getRequestParams() {
        return requestParams;
    }
}
