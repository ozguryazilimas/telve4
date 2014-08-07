/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve;

import com.ozguryazilim.telve.impl.module.TelveModuleExtention;
import java.io.File;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 *
 * @author Hakan Uygun
 */
@RunWith(Arquillian.class)
public class TelveModuleTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies()
                .resolve().withTransitivity().asFile();
        
        WebArchive result = ShrinkWrap.create(WebArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        for (File file : libs) {
            result.addAsLibrary(file);
        }

        System.out.println( result );
        
        return result;
    }

    @Inject
    TelveModuleExtention telveModules;
    
    /**
     * Telve Module Ragitery işleminin çalışıp çalışmadığını test ediyoruz.
     * 
     * telveModules içerisinde tanımlı en az TelveCoreModule olmalı.
     * 
     */
    @Test
    public void telveModuleTest() {
        Assert.assertNotNull("Telve Core Register olmamış", telveModules.isModuleRegistered("TelveCoreModule"));
    }
}
