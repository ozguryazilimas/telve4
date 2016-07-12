/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author oyas
 */
public class UnitNameTest {
    
    public UnitNameTest() {
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
     * Test of getDimension method, of class UnitName.
     */
    @org.junit.Test
    public void testGetDimension() {
        System.out.println("getDimension");
        
        UnitName instance = new UnitName("MASS", "GRAM");
        String expResult = "MASS";
        String result = instance.getDimension();
        assertEquals(expResult, result);
        
    }

    
    /**
     * Test of getName method, of class UnitName.
     */
    @org.junit.Test
    public void testGetName() {
        System.out.println("getName");
        UnitName instance = new UnitName("MASS", "GRAM");
        String expResult = "GRAM";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of of method, of class UnitName.
     */
    @org.junit.Test
    public void testOf() {
        System.out.println("of");
        
        String unitName = "MASS:GRAM";
        UnitName expResult = new UnitName("MASS", "GRAM");
        UnitName result = UnitName.of(unitName);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of of method, of class UnitName.
     */
    @org.junit.Test
    public void testToString() {
        System.out.println("of");
        
        String expResult = "MASS:GRAM";
        UnitName instance = new UnitName("MASS", "GRAM");
        String result = instance.toString();
        assertEquals(expResult, result);
        
    }

    
}
