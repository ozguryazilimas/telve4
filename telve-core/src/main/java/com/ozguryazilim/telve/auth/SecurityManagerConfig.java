/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.realm.Realm;

/**
 * Telve Security Config modeli. 
 * 
 * Uygulamalar tarafından Produce edilmesi gerekir.
 * 
 * Örnek Kullanım :
 * <pre>
 *   @Produces
 *   public SecurityManagerConfig securityManagerConfigProducer(){
 *       return SecurityManagerConfig.create().addRealm(telveIdmRealm);
 *   }
 * </pre>
 * 
 * @author Hakan Uygun
 */
public class SecurityManagerConfig {
    
    
    private List<Realm> realms = new ArrayList<>();
    
    
    public static SecurityManagerConfig create(){
        return new SecurityManagerConfig();
    }
    
    public SecurityManagerConfig addRealm( Realm realm ){
        realms.add(realm);
        return this;
    }
    
    public List<Realm> getRealms(){
        return realms;
    }
    
}
