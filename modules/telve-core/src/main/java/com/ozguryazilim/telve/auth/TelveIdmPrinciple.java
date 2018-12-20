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
