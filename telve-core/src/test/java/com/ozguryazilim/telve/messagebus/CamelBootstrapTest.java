/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus;

import com.ozguryazilim.telve.messagebus.command.Command;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import javax.inject.Inject;
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
                .addClass(TestCommand.class)
                //.addClass(CamelBootstrapTest.class)
                .addPackage("com.ozguryazilim.telve.messagebus.command")
                .addAsWebInfResource("apache-deltaspike.properties",
                        ArchivePaths.create("classes/META-INF/apache-deltaspike.properties"))
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("commandExtention",
                        ArchivePaths.create("classes/META-INF/services/javax.enterprise.inject.spi.Extension"));

        //Bağımlılıkları ekle
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "org.apache.deltaspike.core:deltaspike-core-api",
                        "org.apache.deltaspike.core:deltaspike-core-impl",
                        "com.google.guava:guava",
                        "org.apache.camel:camel-core",
                        "io.astefanutti.camel.cdi:camel-cdi")
                .withTransitivity()
                .asFile());

        System.out.println(archive);

        return archive;
    }

    @Inject
    private CommandSender commandSender;

    @Test
    public void testCamelBootstrap() {
        System.out.println("testCamelBootstrap");
    }

    
    @Test
    public void testDefaultCommand() {
        System.out.println("DEF CMD1");
        
        commandSender.sendCommand(new Command() {

            @Override
            public String getName() {
                return "CDM1";
            }
        });
        System.out.println("CMD2");
        commandSender.sendCommand(new Command() {

            @Override
            public String getName() {
                return "CMD2";
            }
        });
        System.out.println("CMD3");
        commandSender.sendCommand(new TestCommand());
        System.out.println("CMD4");
        commandSender.sendCommand(new TestCommand());
        System.out.println("DEF CMD-END");
    }

    @Test
    public void testScheduledCommand() throws InterruptedException {
        System.out.println("testScheduledCommand");
        
        commandSender.sendCommand(new ScheduledCommand( "123456", "NA", "TestSuit", new TestCommand() ));
        
        System.out.println("testScheduledCommand end");
        Thread.sleep(1000l);
    }
    
}
