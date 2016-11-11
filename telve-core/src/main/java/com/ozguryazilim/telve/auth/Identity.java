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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    private static final Logger LOG = LoggerFactory.getLogger(Identity.class);
    
    @Inject
    private UserService userService;
    
    /**
     * Current User login name'ini döndürür.
     * Eğer bir oturum içerisinde çağırılmamış ise SYSTEM döner.
     * 
     * Bu davranış özellikle repository'ler, gün sonu işlemleri, auditLog gibi yerler için kullanılıyor.
     * @see UserRepository
     * 
     * @return 
     */
    public String getLoginName(){
        try{
            Subject currentUser = SecurityUtils.getSubject();
            if( currentUser.getPrincipal() instanceof TelveIdmPrinciple){
                return ((TelveIdmPrinciple)currentUser.getPrincipal()).getName();
            }
            return currentUser.getPrincipal().toString();
        } catch ( Exception ex ){
            LOG.debug("Current User not found.", ex);
            return "SYSTEM";
        }
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
    
    public List<String> getGroupsMembers(){
        return userService.getUserGroupsMembers(getLoginName());
    }
    
    /**
     * Geriye kullanıcının temel bilgilerini paket olarak döndürür.
     * @return 
     */
    public UserInfo getUserInfo(){
        return userService.getUserInfo(getLoginName());
    }
}
