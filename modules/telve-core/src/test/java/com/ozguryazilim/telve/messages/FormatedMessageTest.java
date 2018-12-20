package com.ozguryazilim.telve.messages;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author oyas
 */
public class FormatedMessageTest {
    
    public FormatedMessageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test of getMessageFromData method, of class FormatedMessage.
     */
    @Test
    public void testGetMessageFromDataTest() {
        System.out.println("getMessageFromData");
        String pattern = "Merhaba {0}$%&Hakan!";
        
        FormatedMessage instance = new FormatedMessage();
        String expResult = "Merhaba Hakan!";
        String result = instance.getMessageFromData(pattern);
        assertEquals(expResult, result);
        
    }
    
    
    /**
     * Test of getMessageFromData method, of class FormatedMessage.
     */
    @Test
    public void testGetMessageFromDataTest1() {
        System.out.println("getMessageFromData1");
        String pattern = "Merhaba Dünya!";
        
        FormatedMessage instance = new FormatedMessage();
        String expResult = "Merhaba Dünya!";
        String result = instance.getMessageFromData(pattern);
        assertEquals(expResult, result);
        
    }
    
    
    /**
     * Test of getMessageFromData method, of class FormatedMessage.
     */
    @Test
    public void testGetMessageFromDataTest2() {
        System.out.println("getMessageFromData2");
        String pattern = "Tebrikler! {0}, {1} nolu fırsatı yakadı.$%&Hakan$%&OPP-0003";
        
        FormatedMessage instance = new FormatedMessage();
        String expResult = "Tebrikler! Hakan, OPP-0003 nolu fırsatı yakadı.";
        String result = instance.getMessageFromData(pattern);
        assertEquals(expResult, result);
        
    }
    
    
    /**
     * Test of getMessageFromData method, of class FormatedMessage.
     */
    @Test
    public void testGetMessageFromDataTest3() {
        System.out.println("getMessageFromData3");
        String pattern = "Tebrikler! {0}, <a href=\"{2}\">{1}</a> nolu fırsatı yakadı.$%&Hakan$%&OPP-0003$%&/tekir/bisi/bisi.jsf?eid=3";
        
        FormatedMessage instance = new FormatedMessage();
        String expResult = "Tebrikler! Hakan, <a href=\"/tekir/bisi/bisi.jsf?eid=3\">OPP-0003</a> nolu fırsatı yakadı.";
        String result = instance.getMessageFromData(pattern);
        assertEquals(expResult, result);
        
    }
    
    
    
}
