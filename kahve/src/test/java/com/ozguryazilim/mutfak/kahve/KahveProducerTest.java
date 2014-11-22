/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.cache.Cache;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
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
public class KahveProducerTest {
    
    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(KahveStore.class)
                .addClass(KahveEntry.class)
                .addClass(KahveProducer.class)
                .addClass(KahveCacheLoader.class)
                .addClass(KahveCacheWriter.class)
                .addClass(Kahve.class)
                .addClass(UserAware.class)
                .addClass(com.ozguryazilim.mutfak.kahve.annotations.Kahve.class)
                .addClass(KahveTestResourceProducer.class)
                .addClass(KahveKey.class)
                .addClass(TestKahveKey.class)
                .addClass(KahveCriteria.class)
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("test-ds.xml",
                        ArchivePaths.create("test-ds.xml"));

        
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "javax.cache:cache-api",
                        "com.hazelcast:hazelcast",
                        "org.picketlink:picketlink-api"
                )
                .withTransitivity()
                .asFile());
        
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
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'Hakan::key-6', 'value-6' )");
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'admin::key-6', 'value-admin' )");
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'merkez::key-6', 'value-merkez' )");
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'key-6', 'value-system' )");
        
        
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'admin::key-7', 'value-admin' )");
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'merkez::key-7', 'value-merkez' )");
        connection.createStatement().executeUpdate(
            "insert into KAHVE ( KV_KEY, KV_VAL ) values ( 'key-7', 'value-system' )");
    }
    
    @Inject
    private Cache<String, KahveEntry> kahveCache;
    
    @Inject @Default
    private Kahve kahve;
    
    @Inject @UserAware
    private Kahve kahveUA;
    
    
    
    @Test
    public void test1(){
        System.out.println( "Test 1" );
        
        kahveCache.put("key-1", new KahveEntry("value-1"));
    }
    
    @Test
    public void test2(){
        System.out.println( "Test 2" );
        
        KahveEntry expResult = new KahveEntry( "value-2" );
        
        KahveEntry result = kahveCache.get("key-2");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void test3(){
        System.out.println( "Test 3" );
        
        kahve.put("key-4", new KahveEntry("value-4"));
    }
    
    @Test
    public void test4(){
        System.out.println( "Test 4" );
        
        kahveUA.put("key-5", new KahveEntry("value-5"));
    }
    
    /**
     * UserAware sonuç döndürmeli, normali döndürmemeli
     */
    @Test
    public void test5(){
        System.out.println( "Test 5" );
        
        KahveEntry expResult = new KahveEntry( "value-6" );
        
        KahveEntry result = kahve.get("key-6");
        
        System.out.println( result );
        assertNotEquals(expResult, result);
        
        result = kahveUA.get("key-6");
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    /**
     * hem UserAware sonuç hem normali sonuç döndürmemeli
     */
    @Test
    public void test6(){
        System.out.println( "Test 6" );
        
        KahveEntry expResult = new KahveEntry( "value-3" );
        
        KahveEntry result = kahve.get("key-3");
        
        System.out.println( result );
        assertEquals(expResult, result);
        
        result = kahveUA.get("key-3");
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    
    /**
     * Integer put Testi
     */
    @Test
    public void test7(){
        System.out.println( "Test 7" );
        
        KahveEntry expResult = new KahveEntry();
        expResult.setAsInteger( 5 );
        
        kahve.put("KEY-INT", 5 );
        
        KahveEntry result = kahve.get("KEY-INT");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    /**
     * Boolean put Testi
     */
    @Test
    public void test8(){
        System.out.println( "Test 8" );
        
        KahveEntry expResult = new KahveEntry();
        expResult.setAsBoolean(true );
        
        kahve.put("KEY-BOOL", true );
        
        KahveEntry result = kahve.get("KEY-BOOL");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    /**
     * BigDecimal put Testi
     */
    @Test
    public void test9(){
        System.out.println( "Test 9" );
        
        KahveEntry expResult = new KahveEntry();
        expResult.setAsBigDecimal( BigDecimal.TEN);
        
        kahve.put("KEY-BIG", BigDecimal.TEN );
        
        KahveEntry result = kahve.get("KEY-BIG");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    /**
     * Date put Testi
     */
    @Test
    public void test10(){
        System.out.println( "Test 10" );
        
        KahveEntry expResult = new KahveEntry();
        Date d = new Date();
        expResult.setAsDate( d );
        
        kahve.put("KEY-DATE", d );
        
        KahveEntry result = kahve.get("KEY-DATE");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    enum TestEnum{
        VAL1,
        VAL2
    }
    
    /**
     * Enum put Testi
     */
    @Test
    public void test11(){
        System.out.println( "Test 11" );
        
        KahveEntry expResult = new KahveEntry();
        Date d = new Date();
        expResult.setAsEnum( TestEnum.VAL1 );
        
        kahve.put("KEY-ENUM", TestEnum.VAL1 );
        
        KahveEntry result = kahve.get("KEY-ENUM");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    
    /**
     * Default value Testi
     */
    @Test
    public void test12(){
        System.out.println( "Test 12" );
        
        KahveEntry expResult = new KahveEntry( "DefaultValue" );
                
        KahveEntry result = kahve.get("KEY-DEF", "DefaultValue");
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    
    /**
     * KahveKey Default value Testi
     */
    @Test
    public void test13(){
        System.out.println( "Test 13" );
        
        //KahveEntry default value kullanacak
        KahveEntry expResult = new KahveEntry( TestKahveKey.Test1 );
                
        KahveEntry result = kahve.get(TestKahveKey.Test1 );
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    
    /**
     * Enum + KahveKey Put Testi
     */
    @Test
    public void test14(){
        System.out.println( "Test 14" );
        
        KahveEntry expResult = new KahveEntry( TestEnum.VAL1 );
        
        
        kahve.put(TestKahveKey.Test1, TestEnum.VAL1 );
        
        KahveEntry result = kahve.get(TestKahveKey.Test1);
        
        System.out.println( result );
        assertEquals(expResult, result);
    }
    
    /**
     * KahveCriteria testi
     */
    @Test
    public void test15(){
        System.out.println( "Test 15" );
        
        KahveEntry expResult = new KahveEntry( "value-admin" );
        
        KahveCriteria kc = KahveCriteria.create().addScope("admin").addKey("key-6");
        
        System.out.println(kc);
        
        KahveEntry result = kahve.get(kc);
        
        System.out.println( result );
        assertEquals(expResult, result);
        
        result = kahveUA.get(kc);
        System.out.println( result );
        assertEquals("value-6", result.getValue());
    }
    
    /**
     * KahveCriteria testi
     */
    @Test
    public void test16(){
        System.out.println( "Test 16" );
        
        KahveEntry expResult = new KahveEntry( "value-admin" );
        
        KahveCriteria kc = KahveCriteria.create().addScope("admin").addScope("merkez").addKey("key-7");
        
        System.out.println(kc);
        
        KahveEntry result = kahve.get(kc);
        
        System.out.println( result );
        assertEquals(expResult, result);
        
        result = kahveUA.get(kc);
        System.out.println( result );
        assertEquals("value-admin", result.getValue());
    }
    
    
    /**
     * KahveCriteria testi
     */
    @Test
    public void test17(){
        System.out.println( "Test 17" );
        
        KahveEntry expResult = new KahveEntry( "ABC" );
        
        KahveCriteria kc = KahveCriteria.create().addScope("admin").addScope("merkez").addKey("key-8")
                .addDefaultValue(new KahveEntry("ABC"), "merkez");
        
        System.out.println(kc);
        
        KahveEntry result = kahve.get(kc);
        
        System.out.println( result );
        assertEquals(expResult, result);
        
        result = kahveUA.get(kc);
        System.out.println( result );
        assertEquals("ABC", result.getValue());
    }
    
}
