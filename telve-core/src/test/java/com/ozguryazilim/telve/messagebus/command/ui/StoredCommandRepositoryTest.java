/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.messagebus.command.AbstractStorableCommand;
import com.ozguryazilim.telve.messagebus.command.Command;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import java.math.BigDecimal;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * StoredCommandRepository için test caseler.
 * 
 * @author Hakan Uygun
 */
@Dependent
@RunWith(Arquillian.class)
public class StoredCommandRepositoryTest {
 
    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(Command.class)
                .addClass(StorableCommand.class)
                .addClass(AbstractStorableCommand.class)
                .addClass(StoredCommandRepository.class)
                .addClass(TestCommand.class)
                .addClass(TestCommandEditor.class)
                .addClass(CommandEditorBase.class)
                .addPackage("com.ozguryazilim.telve.query")
                .addPackage("com.ozguryazilim.telve.data")
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
                        "com.ozguryazilim.mutfak:telve-core-model"
                )
                .withTransitivity()
                .asFile());

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
                        "com.google.code.gson:gson:2.2.4")
                .withTransitivity()
                .asFile());

        System.out.println(archive);

        return archive;
    }

    @Produces
    @PersistenceContext(name = "test") @Default
    private EntityManager entityManager;

    
    @Inject
    private StoredCommandRepository repository;

    String testJson = "{\"stringData\":\"StringData\",\"integerData\":-2147483648,\"booleanData\":true,\"dateDate\":\"Nov 26, 2014 2:34:55 PM\",\"bigDecimalData\":10,\"name\":\"TestKomutu1\",\"info\":\"deneme bilgisi\",\"createDate\":\"Nov 26, 2014 2:34:55 PM\",\"createBy\":\"TestSuit\"}";
    
    @Test
    public void testSerialize() {
        System.out.println("testSerialize");
        
        TestCommand command = new TestCommand();
        command.setBigDecimalData(BigDecimal.TEN);
        command.setBooleanData(Boolean.TRUE);
        command.setDateDate(new Date());
        command.setIntegerData(Integer.MIN_VALUE);
        command.setName("TestKomutu1");
        command.setStringData("StringData");
        
        String result = repository.serialize(command);
        System.out.println( result );
        //assertEquals( testJson, result );
    }

    @Test
    public void testDeserialize() {
        
        System.out.println("testDeerialize");
        
        TestCommand command = repository.deserialize(testJson, TestCommand.class);
        
        assertEquals( BigDecimal.TEN, command.getBigDecimalData());
        assertEquals( Boolean.TRUE, command.getBooleanData());
        assertEquals( "TestKomutu1", command.getName());
        
    }
    
    
    @Test
    public void testConvert1(){
        System.out.println("testConvert1");
        
        TestCommand command = new TestCommand();
        command.setBigDecimalData(BigDecimal.TEN);
        command.setBooleanData(Boolean.TRUE);
        command.setDateDate(new Date());
        command.setIntegerData(Integer.MIN_VALUE);
        command.setName("TestKomutu1");
        command.setStringData("StringData");
        
        StoredCommand result = repository.convert(command);
        
        assertEquals( "TestKomutu1", result.getName());
        assertEquals( "com.ozguryazilim.telve.messagebus.command.ui.TestCommand", result.getType());
        System.out.println( result.getType() + " " + result.getCommand());
    }
    
    
    @Test
    public void testConvert2() throws ClassNotFoundException{
        System.out.println("testConvert2");
        
        TestCommand command = new TestCommand();
        command.setBigDecimalData(BigDecimal.TEN);
        command.setBooleanData(Boolean.TRUE);
        command.setDateDate(new Date());
        command.setIntegerData(Integer.MIN_VALUE);
        command.setName("TestKomut2");
        command.setStringData("StringData");
        
        StoredCommand sc = new StoredCommand();
        
        sc.setName("TestKomut2");
        sc.setType("com.ozguryazilim.telve.messagebus.command.ui.TestCommand");
        sc.setCommand(repository.serialize(command));
        
        TestCommand result = (TestCommand) repository.convert(sc);
        
        assertEquals( "TestKomut2", result.getName());
    }
    
    @Test
    public void testCommandEditorInit() throws ClassNotFoundException{
        TestCommandEditor ce = new TestCommandEditor();
        
        TestCommand command = new TestCommand();
        command.setBigDecimalData(BigDecimal.TEN);
        command.setBooleanData(Boolean.TRUE);
        command.setDateDate(new Date());
        command.setIntegerData(Integer.MIN_VALUE);
        command.setName("TestKomut2");
        command.setStringData("StringData");
        
        StoredCommand sc = new StoredCommand();
        
        sc.setName("TestKomut2");
        sc.setType("com.ozguryazilim.telve.messagebus.command.ui.TestCommand");
        sc.setCommand(repository.serialize(command));
        
        ce.init((StorableCommand)repository.convert(sc));
        
        assertEquals( "TestKomut2", ce.getCommand().getName());
        assertEquals( BigDecimal.TEN, ce.getCommand().getBigDecimalData());
    }
    
}
