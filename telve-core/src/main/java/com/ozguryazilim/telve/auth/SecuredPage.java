/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import org.apache.deltaspike.security.api.authorization.Secured;

/**
 * Sayfaların Yetki kontrollerini yapar.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Secured(PicketLinkAccessDecisionVoter.class)
public @interface SecuredPage {
    
}
