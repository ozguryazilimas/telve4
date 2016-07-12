/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import com.ozguryazilim.telve.unit.dimensions.MassDimension;
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
public class MassDimensionTest {
    
    public MassDimensionTest() {
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
     * Test of getDimensionName method, of class MassDimension.
     */
    @Test
    public void testGetDimensionName() {
        System.out.println("getDimensionName");
        MassDimension instance = new MassDimension();
        String expResult = "MASS";
        String result = instance.getDimensionName();
        assertEquals(expResult, result);
        
    }
    
    
    /**
     * Test of getDimensionName method, of class MassDimension.
     */
    @Test
    public void testGetUnitChain() throws UnitException {
        System.out.println("getUnitChain");
        MassDimension instance = new MassDimension();
        String expResult = "MASS";
        String result = instance.getDimensionName();
        
        System.out.println(instance.getUnitChain(MassDimension.KILOGRAM_UNIT));
        System.out.println(instance.getUnitChain(MassDimension.GRAM_UNIT));
        
        assertEquals(expResult, result);
        
    }
    
}
