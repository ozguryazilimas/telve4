/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author oyas
 */
public class CustomDimensionTest {
    
    public CustomDimensionTest() {
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
     * Test of getDimensionName method, of class CustomDimension.
     */
    @Test
    public void testCustomDimention() throws UnitException {
        System.out.println("CustomDimention");
        
        UnitSetBuilder.create("ZAMAN", "SAAT")
                .addUnit("GUN", Quantities.of( new BigDecimal(24), "ZAMAN:SAAT"))
                .addUnit("ISGUN", Quantities.of( new BigDecimal(8), "ZAMAN:SAAT"))
                .addUnit("EGGUN", Quantities.of( new BigDecimal(6), "ZAMAN:SAAT"))
                .register();
        
        UnitSet de = UnitSetRegistery.getUnitSet("ZAMAN");
        
        System.out.println(de.getUnitNames());
     
        
        Quantity q = Quantities.of(new BigDecimal(100), "ZAMAN:ISGUN");
        Quantity q2 = Quantities.convert(q, UnitName.of("ZAMAN:EGGUN"));
        
        System.out.println(q);
        System.out.println(q2);
        
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
