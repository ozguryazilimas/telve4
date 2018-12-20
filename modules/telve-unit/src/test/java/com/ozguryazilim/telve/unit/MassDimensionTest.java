package com.ozguryazilim.telve.unit;

import com.ozguryazilim.telve.unit.sets.MassUnitSet;
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
     * Test of getDimensionName method, of class MassUnitSet.
     */
    @Test
    public void testGetDimensionName() {
        System.out.println("getDimensionName");
        MassUnitSet instance = new MassUnitSet();
        String expResult = "MASS";
        String result = instance.getDimensionName();
        assertEquals(expResult, result);
        
    }
    
    
    /**
     * Test of getDimensionName method, of class MassUnitSet.
     */
    @Test
    public void testGetUnitChain() throws UnitException {
        System.out.println("getUnitChain");
        MassUnitSet instance = new MassUnitSet();
        String expResult = "MASS";
        String result = instance.getDimensionName();
        
        System.out.println(instance.getUnitChain(MassUnitSet.KILOGRAM_UNIT));
        System.out.println(instance.getUnitChain(MassUnitSet.GRAM_UNIT));
        
        assertEquals(expResult, result);
        
    }
    
}
