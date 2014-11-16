/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * CDICamelContext Bootstrap Testi
 * 
 * @author Hakan Uygun
 */
@RunWith(Arquillian.class)
public class CamelBootstrapTest {
   
    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(CamelBootstrap.class)
                .addClass(TelveCamelContext.class)
                .addClass(SimpleTestRoute.class)
                .addClass(CamelBootstrapTest.class)
                .addAsWebInfResource("apache-deltaspike.properties",
                        ArchivePaths.create("classes/META-INF/apache-deltaspike.properties"))
                .addAsWebInfResource("test-persistence.xml",
                        ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-ds.xml",
                        ArchivePaths.create("test-ds.xml"))
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("test-orm.xml",
                        ArchivePaths.create("classes/META-INF/orm.xml"));

        
        //Bağımlılıkları ekle
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "org.apache.deltaspike.core:deltaspike-core-api",
                        "org.apache.deltaspike.core:deltaspike-core-impl",
                        "org.apache.deltaspike.modules:deltaspike-partial-bean-module-api",
                        "org.apache.deltaspike.modules:deltaspike-partial-bean-module-impl",
                        "org.apache.deltaspike.modules:deltaspike-jpa-module-api",
                        "org.apache.deltaspike.modules:deltaspike-jpa-module-impl",
                        "org.apache.deltaspike.modules:deltaspike-data-module-api",
                        "org.apache.deltaspike.modules:deltaspike-data-module-impl",
                        "org.apache.camel:camel-core",
                        "io.astefanutti.camel.cdi:camel-cdi")
                .withTransitivity()
                .asFile());

        System.out.println(archive);

        return archive;
    }
    
    @Test
    public void testCamelBootstrap(){
        System.out.println("testCamelBootstrap");
    }
    
}
