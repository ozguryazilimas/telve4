/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve;

import com.ozguryazilim.telve.impl.module.TelveModuleExtention;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;

/**
 *
 * @author martin
 */
@RequestScoped
@Named("HedeBean")
public class NewSessionBean {

    @Inject
    TelveModuleExtention telveModules;
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject @Named("messages")
    Map<String,String> messages;
    
    public void businessMethod() {
        
        System.out.println( "Hede modulu var mı? " +  telveModules.isModuleRegistered("Hede"));
        System.out.println( "Module List : " +  telveModules.getModuleNames());
        
        System.out.println( ConfigResolver.getPropertyValue("Deneme", "deneme"));
        System.out.println( ConfigResolver.getPropertyValue("Ahmet"));
        System.out.println( ConfigResolver.getAllProperties());
        
        System.out.println(messages.get("deneme"));
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    public void test(){
        //viewConfigResolver.getViewConfigDescriptor("").getMetaData().
    }
    
}
