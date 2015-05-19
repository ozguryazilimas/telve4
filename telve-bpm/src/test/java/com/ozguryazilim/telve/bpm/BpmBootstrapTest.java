/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

/**
 * jBPM 6.2 BootStrap Test
 * 
 * @author Hakan Uygun
 */
@RunWith(Arquillian.class)
public class BpmBootstrapTest {
    

    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addPackage("com.ozguryazilim.telve.bpm")
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

        //Model paketini ekle
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "org.apache.deltaspike.core:deltaspike-core-api",
                        "org.apache.deltaspike.core:deltaspike-core-impl",
                        "com.ozguryazilim.mutfak:telve-core",
                        "org.kie:kie-api",
                        "org.jbpm:jbpm-human-task-core",
                        "org.jbpm:jbpm-kie-services",
                        "org.jbpm:jbpm-services-api",
                        "org.jbpm:jbpm-services-cdi"
                )
                .withTransitivity()
                .asFile());

        System.out.println("Arşiv tamam");
        
        return archive;
    }

    /**
     * Test of init method, of class BpmBootstrap.
     */
    @org.junit.Test
    public void testInit() {
        System.out.println("init");
        
    }
    
}
