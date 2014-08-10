/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.impl.module;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.api.module.TelveModuleRegistery;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

/**
 * Telve Module tanımlarını tarayıp bir yere toplar
 *
 * @author Hakan Uygun
 */
public class TelveModuleExtention implements Extension {

    private static final Logger LOG = Logger.getLogger(TelveModuleExtention.class.getName());

    private final Set<Bean<?>> startupBeans = new LinkedHashSet<Bean<?>>();
    
    <T> void processBean(@Observes ProcessBean<T> event) {
        if (event.getAnnotated().isAnnotationPresent(TelveModule.class)) {
            TelveModule a = event.getAnnotated().getAnnotation(TelveModule.class);
            String moduleName = a.name();
            if( moduleName.isEmpty() ){
                moduleName = event.getBean().getBeanClass().getSimpleName();
            }
            System.out.println("Found Module: " + moduleName);
            startupBeans.add(event.getBean());
            
            String ribbonFile = a.ribbon();
            if( ribbonFile.isEmpty() ){
                ribbonFile = moduleName + ".rbn.xml";
            }
            
            String permFile = a.permissions();
            if( permFile.isEmpty() ){
                permFile = moduleName + ".perm.xml";
            }
            
            String messageFile = a.messages();
            if( messageFile.isEmpty() ){
                messageFile = moduleName + "-messages";
            }
            
            TelveModuleRegistery.register(moduleName, permFile, ribbonFile, messageFile);
        }
    }

    /**
     * Module Beanlerini Start eder. Böylece @Startup etkisi gösterir.
     * @param event
     * @param manager 
     */
    void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
        for (Bean<?> bean : startupBeans) {
            // the call to toString() is a cheat to force the bean to be initialized
            manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean)).toString();
        }
    }

    /**
     * Geriye sisteme tanımlı olan module listeni döndürür.
     * @return 
     */
    public List<String> getModuleNames() {
        return TelveModuleRegistery.getModuleNames();
    }

    /**
     * Adı verilen modulün tanımlı olup olmadığını döndürür.
     * @param moduleName
     * @return 
     */
    public boolean isModuleRegistered( String moduleName ){
        return TelveModuleRegistery.isModuleRegistered(moduleName);
    }

}
