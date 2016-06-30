/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro Subject çevresine bir wrapper.
 * 
 * PicketLink için tasarlanmış API'lere uyumlu olması için. Ayrıca bazı kolay erişim için temel fonksiyonlar.
 * 
 * @author Hakan Uygun
 */
@Named
@RequestScoped
public class Identity {
    
    @Inject
    private UserService userService;
    
    public String getLoginName(){
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.getPrincipal().toString();
    }
    
    public Boolean hasPermission( String domain, String action ){
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermitted( domain + ":" + action );
    }
    
    public Boolean isPermitted( String permission ){
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermitted( permission );
    }
    
    
    /**
     * Kullanıcın gerçek adını döndürür.
     * @return 
     */
    public String getUserName(){
        return userService.getUserName(getLoginName());
    }
    
    /**
     * currentUser'ın Role Listesini döndürür.
     * @return 
     */
    public List<String> getRoles(){
        return userService.getUserRoles(getLoginName());
    }
    
    /**
     * currentUser'ın UnifiedRole Listesini döndürür.
     * @return 
     */
    public List<String> getUnifiedRoles(){
        return userService.getUserUnifiedRoles(getLoginName());
    }
    
    /**
     * currentUser'ın grup listesini döndürür.
     * @return 
     */
    public List<String> getGroups(){
        return userService.getUserGroups(getLoginName());
    }
}
