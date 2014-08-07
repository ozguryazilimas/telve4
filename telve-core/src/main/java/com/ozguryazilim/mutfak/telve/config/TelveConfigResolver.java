/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.mutfak.telve.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.picketlink.Identity;

/**
 * Kullanıcı ve ProjectStage gön önünde bulundurarak Sistem içinde tanımlı konfig bilgilerini ayarlar.
 * 
 * DeltaSpike ConfigResolver üzerine Veri tabanını kullanır.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class TelveConfigResolver {
    
    @Inject
    @Any
    private Identity identity;
    
    public String getProperty( String key ){
        
        String account = identity.getAccount().getId();
        
        String result = ConfigResolver.getPropertyValue( account + "." + key);
        
        if( result == null ){
            result = ConfigResolver.getPropertyValue( key );
        }
        
        return result;
    }
    
}
