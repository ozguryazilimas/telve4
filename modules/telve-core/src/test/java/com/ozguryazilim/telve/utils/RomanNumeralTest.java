package com.ozguryazilim.telve.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author oyas
 */
public class RomanNumeralTest {
    

    /**
     * Test of toString method, of class RomanNumeral.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        RomanNumeral instance = new RomanNumeral(123);
        String expResult = "CXXIII";
        String result = instance.toString();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of toInt method, of class RomanNumeral.
     */
    @Test
    public void testToInt() {
        System.out.println("toInt");
        RomanNumeral instance = new RomanNumeral("CXXIII");
        int expResult = 123;
        int result = instance.toInt();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class RomanNumeral.
     */
    @Test
    public void testConverter() {
        System.out.println("converter");
        
        String expResult = "CXXIII";
        String result = RomanNumeral.integerToRomanNumeral(123);
        assertEquals(expResult, result);
        
    }
    
}
