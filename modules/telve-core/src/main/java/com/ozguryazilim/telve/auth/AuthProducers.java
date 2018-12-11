/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.deltaspike.data.api.audit.CurrentUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auth işlemleri için gereken producaerlar.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
public class AuthProducers {

    private static final Logger LOG = LoggerFactory.getLogger(AuthProducers.class);
            
    
    @Produces
    @Named
    @RequestScoped
    public Subject getCurrentUser() {
        Subject s = SecurityUtils.getSubject();

        return SecurityUtils.getSubject();
    }

    /**
     * DeltaSpike Audit için kullanılıyor.
     *
     * @return
     */
    @Produces
    @CurrentUser
    public String currentUser() {
        try {
            Subject s = SecurityUtils.getSubject();
            if (s.getPrincipal() instanceof TelveIdmPrinciple) {
                return ((TelveIdmPrinciple) s.getPrincipal()).getName();
            }

            return s.getPrincipal().toString();
        } catch (UnavailableSecurityManagerException ex) {
            //Normal kullanıcı oturumu dışında ( zamanlanmış görev v.b. ) çalıştığında doğal olarak security manager yok!
            LOG.debug("Current User Not Found: {}", ex );
            return "SYSTEM";
        }
    }

}
