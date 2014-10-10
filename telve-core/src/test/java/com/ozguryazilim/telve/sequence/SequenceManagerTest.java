/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.sequence;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Sequance Manager Test Case
 * @author Hakan Uygun 
 */
@RunWith(Arquillian.class)
public class SequenceManagerTest {
    
    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(SequenceManager.class)
                .addClass(SequenceStore.class)
                .addClass(SequenceMemoryStore.class)
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"));
                /*
                .addAsWebInfResource("test-persistence.xml",
                        ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-ds.xml",
                        ArchivePaths.create("test-ds.xml"))
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("test-orm.xml",
                        ArchivePaths.create("classes/META-INF/orm.xml"));
                */

        
        System.out.println(archive);

        return archive;
    }
    
    //@Produces @RequestScoped @Default
    public SequenceStore produceStore(){
        return new SequenceMemoryStore();
    }
    
    @Inject
    private SequenceManager sequenceManager;
    
    /**
     * Test of getNewNumber method, of class SequenceManager.
     */
    @Test
    public void testGetNewNumber() {
        System.out.println("getNewNumber");
        String serial = "AA";
        Integer len = 6;
        
        String expResult = "000001";
        String result = sequenceManager.getNewNumber(serial, len);
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getNewNumber method, of class SequenceManager.
     */
    @Test
    public void testGetNewSerialNumber() {
        System.out.println("getNewNumber");
        String serial = "BB";
        Integer len = 6;
        
        String expResult = "BB-000001";
        String result = sequenceManager.getNewSerialNumber(serial, len);
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
}
