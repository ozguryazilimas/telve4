/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

/**
 * Shiro auth olan kişi/login name için interface.
 * 
 * @author Hakan Uygun
 */
public interface TelveIdmPrinciple {
    
    /**
     * Login/User Name geri döndürür.
     * @return 
     */
    String getName();
    
}
