/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author haky
 */
@RunWith(Arquillian.class)
public class KahveStoreTest {
    
    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(KahveStore.class)
                .addClass(KahveEntry.class)
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("test-ds.xml",
                        ArchivePaths.create("test-ds.xml"));

        
        System.out.println(archive);

        return archive;
    }
    
    @Before
    public void setUp() throws NamingException, SQLException {
        InitialContext ic = new InitialContext();
        DataSource dataSource = (DataSource) ic.lookup("java:jboss/datasources/TestDS");
        
        Connection connection = dataSource.getConnection();
        
        connection.createStatement().executeUpdate(
            "create table if not exists KAHVE ( KV_KEY varchar(255), KV_VAL varchar(255))");
        
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'key-2', 'value-2' )");
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'key-3', 'value-3' )");
        
        //KahveStore.createInstance("java:jboss/datasources/TestDS");
        KahveStore.createInstance( dataSource);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class KahveStore.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        KahveStore expResult = null;
        KahveStore result = KahveStore.getInstance();
        assertNotEquals(expResult, result);
    }

    /**
     * Test of save method, of class KahveStore.
     */
    @Test
    public void testSave() throws SQLException {
        System.out.println("save");
        String s = "key-1";
        KahveEntry o = new KahveEntry( "value-1");
        KahveStore instance = KahveStore.getInstance();
        instance.save(s, o);
        
        KahveEntry o2 = instance.load("key-1");
        assertEquals(o, o2);
    }
    
    /**
     * Test of save method, of class KahveStore.
     */
    @Test
    public void testUpdate()  {
        System.out.println("save");
        String s = "key-2";
        KahveEntry o = new KahveEntry( "value-1");
        KahveStore instance = KahveStore.getInstance();
        try {
            instance.save(s, o);
        } catch (SQLException ex) {
            Logger.getLogger(KahveStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        KahveEntry o2 = instance.load("key-2");
        assertEquals(o, o2);
    }

    /**
     * Test of delete method, of class KahveStore.
     */
    @Test
    public void testDelete() throws SQLException {
        System.out.println("delete");
        String s = "key-3";
        KahveStore instance = KahveStore.getInstance();
        instance.delete(s);
    }

    /**
     * Test of load method, of class KahveStore.
     */
    @Test
    public void testLoad() {
        System.out.println("load");
        String s = "key-2";
        KahveStore instance = KahveStore.getInstance();
        KahveEntry expResult = new KahveEntry( "value-2" );
        KahveEntry result = instance.load(s);
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
}
