/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.telve;

import com.ozguryazilim.telve.NewSessionBean;
import java.io.File;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author haky
 */
@RunWith(Arquillian.class)
public class NewSessionBeanTest {

    @Deployment
    public static WebArchive createDeployment() {
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
                .resolve().withTransitivity().asFile();
        
        WebArchive result = ShrinkWrap.create(WebArchive.class)
                .addClass(NewSessionBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        for (File file : libs) {
            result.addAsLibrary(file);
        }

        System.out.println( result );
        
        return result;
    }

    @Inject
    NewSessionBean instance;

    public NewSessionBeanTest() {
    }

    /**
     * Test of businessMethod method, of class NewSessionBean.
     */
    @Test
    public void testBusinessMethod() {
        System.out.println("businessMethod");
        instance.businessMethod();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
