/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Kahve'nin userAware çalışması için bir String olarak mevcut kullanıcıyı göndermek gerekiyor.
 * 
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class KahveIdentityProducer implements Serializable{
    
    @Inject
    private Identity identity;
    
    /**
     * UserAware Kahve için gerekli
     * @return 
     */
    @Produces @UserAware
    public String produceUserName(){
        return identity.getLoginName();
    }
    
}
