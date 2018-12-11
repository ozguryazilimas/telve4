/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.sequence;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ArchivePaths;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * FIXME: Arquillian için test örneği.  Daha sonra bağımlılıklarla beraber düzenlenecek.
 
 * @author Hakan Uygun
 */
//@RunWith(Arquillian.class)
public class SequenceJpaStoreTest {


    // @Deployment
    // public static WebArchive deployment() {

    //     WebArchive archive = ShrinkWrap.create(WebArchive.class)
    //             .addClass(SequenceStore.class)
    //             .addClass(SequenceJpaStore.class)
    //             .addAsWebInfResource("apache-deltaspike.properties",
    //                     ArchivePaths.create("classes/META-INF/apache-deltaspike.properties"))
    //             .addAsWebInfResource("test-persistence.xml",
    //                     ArchivePaths.create("classes/META-INF/persistence.xml"))
    //             .addAsWebInfResource("test-ds.xml",
    //                     ArchivePaths.create("test-ds.xml"))
    //             .addAsWebInfResource("test-beans.xml",
    //                     ArchivePaths.create("beans.xml"))
    //             .addAsWebInfResource("test-orm.xml",
    //                     ArchivePaths.create("classes/META-INF/orm.xml"));

    //     //Model paketini ekle
    //     archive.addAsLibraries(
    //             Maven.resolver().loadPomFromFile("pom.xml").resolve(
    //                     "com.ozguryazilim.mutfak:telve-core-model"
    //             )
    //             .withTransitivity()
    //             .asFile());

        
    //     System.out.println(archive);

    //     return archive;
    // }
    
    // @Produces
    // @PersistenceContext(name = "test") @Default
    // private EntityManager entityManager;
    
    // @Inject
    // private SequenceJpaStore jpaStore;

    // /**
    //  * Test of findLastValue method, of class SequenceJpaStore.
    //  */
    // @Test
    // public void testFindLastValue() {
    //     System.out.println("findLastValue");
    //     String key = "SS";
    //     Long expResult = 0l;
    //     Long result = jpaStore.findLastValue(key);
    //     assertEquals(expResult, result);
    // }

    // /**
    //  * Test of findNextValue method, of class SequenceJpaStore.
    //  */
    // @Test
    // public void testFindNextValue() {
    //     System.out.println("findNextValue");
    //     String key = "TT";
    //     Long expResult = 1l;
    //     Long result = jpaStore.findNextValue(key);
    //     assertEquals(expResult, result);
    // }

    // /**
    //  * Test of getNextValue method, of class SequenceJpaStore.
    //  */
    // @Test
    // public void testGetNextValue() {
    //     System.out.println("getNextValue");
    //     String key = "AA";
    //     Long expResult = 1l;
    //     Long result = jpaStore.getNextValue(key);
    //     assertEquals(expResult, result);
    // }

    // /**
    //  * Test of saveValue method, of class SequenceJpaStore.
    //  */
    // @Test
    // public void testSaveValue() {
    //     System.out.println("saveValue");
    //     String key = "BB";
    //     Long expResult = 3l;
    //     jpaStore.saveValue(key, 3l);
    //     Long result = jpaStore.findLastValue(key);
    //     assertEquals(expResult, result);
    // }

    // /**
    //  * Test of findSequence method, of class SequenceJpaStore.
    //  */
    // @Test
    // public void testFindSequence() {
    //     System.out.println("findSequence");
    //     String key = "CC";
    //     Long expResult = 0l;
    //     Long result = jpaStore.findLastValue(key);
    //     assertEquals(expResult, result);
    // }
    
}
