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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Auth işlemleri için gereken producaerlar.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class AuthProducers {
   
    @Produces
    @Named
    @RequestScoped
    public Subject getCurrentUser(){
        Subject s = SecurityUtils.getSubject();
        
        return SecurityUtils.getSubject();
    }
    
        
}
