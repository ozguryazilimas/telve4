/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import com.ozguryazilim.telve.unit.sets.MassUnitSet;
import java.math.BigDecimal;
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
public class QuantityTest {
    
    public QuantityTest() {
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

    
    private static final Quantity TEN_GRAM = new Quantity(BigDecimal.TEN, MassUnitSet.GRAM);
    
    /**
     * Test of getAmount method, of class Quantity.
     */
    @Test
    public void testGetAmount() {
        System.out.println("getAmount");
        Quantity instance = new Quantity(BigDecimal.ONE, MassUnitSet.GRAM);
        BigDecimal expResult = BigDecimal.ONE;
        BigDecimal result = instance.getAmount();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getUnitName method, of class Quantity.
     */
    @Test
    public void testGetUnitName() {
        System.out.println("getUnitName");
        Quantity instance = new Quantity(BigDecimal.ONE, MassUnitSet.GRAM);
        UnitName expResult = MassUnitSet.GRAM;
        UnitName result = instance.getUnitName();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_BigDecimal_UnitName() {
        System.out.println("of");
        BigDecimal a = BigDecimal.TEN;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_BigDecimal_String() {
        System.out.println("of");
        BigDecimal a = BigDecimal.TEN;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_long_String() {
        System.out.println("of");
        long a = 10L;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_int_String() {
        System.out.println("of");
        int a = 10;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_double_String() {
        System.out.println("of");
        double a = 10.0;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_float_String() {
        System.out.println("of");
        float a = 10.0F;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_String_String() {
        System.out.println("of");
        String a = "10";
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Long_String() {
        System.out.println("of");
        Long a = 10l;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Integer_String() {
        System.out.println("of");
        Integer a = 10;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Double_String() {
        System.out.println("of");
        Double a = 10.0;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Float_String() {
        System.out.println("of");
        Float a = 10.0F;
        String un = "MASS:GRAM";
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_long_UnitName() {
        System.out.println("of");
        long a = 10L;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_int_UnitName() {
        System.out.println("of");
        int a = 10;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_double_UnitName() {
        System.out.println("of");
        double a = 10.0;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_float_UnitName() {
        System.out.println("of");
        float a = 10.0F;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_String_UnitName() {
        System.out.println("of");
        String a = "10";
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Long_UnitName() {
        System.out.println("of");
        Long a = 10l;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Integer_UnitName() {
        System.out.println("of");
        Integer a = 10;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Double_UnitName() {
        System.out.println("of");
        Double a = 10.0;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class Quantity.
     */
    @Test
    public void testOf_Float_UnitName() {
        System.out.println("of");
        Float a = 10.0F;
        UnitName un = MassUnitSet.GRAM;
        Quantity expResult = TEN_GRAM;
        Quantity result = Quantity.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of toString method, of class Quantity.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Quantity instance = TEN_GRAM;
        String expResult = "MASS:GRAM(10)";
        String result = instance.toString();
        assertEquals(expResult, result);
        
    }


    /**
     * Test of convert method, of class Quantity.
     */
    @Test
    public void testConvert() throws Exception {
        System.out.println("convert");
        UnitName unitName = MassUnitSet.DECIGRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(100, MassUnitSet.DECIGRAM);
        Quantity result = instance.convert(unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of add method, of class Quantity.
     */
    @Test
    public void testAdd_BigDecimal() {
        System.out.println("add");
        BigDecimal amount = BigDecimal.TEN;
        Quantity instance = Quantity.of(1, MassUnitSet.GRAM);
        Quantity expResult = Quantity.of(11, MassUnitSet.GRAM);
        Quantity result = instance.add(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of add method, of class Quantity.
     */
    @Test
    public void testAdd_Quantity() throws Exception {
        System.out.println("add");
        Quantity that = TEN_GRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(20, MassUnitSet.GRAM);
        Quantity result = instance.add(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of add method, of class Quantity.
     */
    @Test
    public void testAdd_Quantity_UnitName() throws Exception {
        System.out.println("add");
        Quantity that = TEN_GRAM;
        UnitName unitName = MassUnitSet.DECIGRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(200, MassUnitSet.DECIGRAM);
        Quantity result = instance.add(that, unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of subtract method, of class Quantity.
     */
    @Test
    public void testSubtract_BigDecimal() {
        System.out.println("subtract");
        BigDecimal amount = BigDecimal.ONE;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(9, MassUnitSet.GRAM);
        Quantity result = instance.subtract(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of subtract method, of class Quantity.
     */
    @Test
    public void testSubtract_Quantity() throws Exception {
        System.out.println("subtract");
        Quantity that = Quantity.of(1, MassUnitSet.GRAM);
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(9, MassUnitSet.GRAM);
        Quantity result = instance.subtract(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of subtract method, of class Quantity.
     */
    @Test
    public void testSubtract_Quantity_UnitName() throws Exception {
        System.out.println("subtract");
        Quantity that = Quantity.of(1, MassUnitSet.GRAM);
        UnitName unitName = MassUnitSet.DECIGRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(90, MassUnitSet.DECIGRAM);
        Quantity result = instance.subtract(that, unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of multiply method, of class Quantity.
     */
    @Test
    public void testMultiply_BigDecimal() {
        System.out.println("multiply");
        BigDecimal amount = BigDecimal.TEN;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(100, MassUnitSet.GRAM);
        Quantity result = instance.multiply(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of multiply method, of class Quantity.
     */
    @Test
    public void testMultiply_Quantity() throws Exception {
        System.out.println("multiply");
        Quantity that = TEN_GRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(100, MassUnitSet.GRAM);
        Quantity result = instance.multiply(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of multiply method, of class Quantity.
     */
    @Test
    public void testMultiply_Quantity_UnitName() throws Exception {
        System.out.println("multiply");
        Quantity that = TEN_GRAM;
        UnitName unitName = MassUnitSet.DECIGRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(10000, MassUnitSet.DECIGRAM);;
        Quantity result = instance.multiply(that, unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of divide method, of class Quantity.
     */
    @Test
    public void testDivide_BigDecimal() {
        System.out.println("divide");
        BigDecimal amount = new BigDecimal(2.5);
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(4, MassUnitSet.GRAM);
        Quantity result = instance.divide(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of divide method, of class Quantity.
     */
    @Test
    public void testDivide_Quantity() throws Exception {
        System.out.println("divide");
        Quantity that = Quantity.of(2.5, MassUnitSet.GRAM);
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(4, MassUnitSet.GRAM);
        Quantity result = instance.divide(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of divide method, of class Quantity.
     * //TODO: Bu test bir daha incelenmeli hesap hatası var gibi
     */
    @Test
    public void testDivide_Quantity_UnitName() throws Exception {
        System.out.println("divide");
        Quantity that = Quantity.of(2.5, MassUnitSet.GRAM);;
        UnitName unitName = MassUnitSet.DECIGRAM;
        Quantity instance = TEN_GRAM;
        Quantity expResult = Quantity.of(4, MassUnitSet.DECIGRAM);
        Quantity result = instance.divide(that, unitName);
        assertEquals(expResult, result);
    }
    
    
    /**
     * 
     * PI testi
     * 
     * FIXME: Burada sanırım bir hata yapıyoruz.
     * 
     */
    @Test()
    public void testdivide7() throws UnitException {
        System.out.println("divide7");
                
        Quantity q1 = Quantity.of(22, MassUnitSet.GRAM);
        Quantity q2 = Quantity.of(7, MassUnitSet.GRAM);
        Quantity expResult = Quantity.of("3.142857142857143", MassUnitSet.GRAM);
        
        Quantity result = q1.divide(q2, MassUnitSet.GRAM);
        
        assertEquals(expResult, result);
        
    }
    
    /**
     * 
     * Ondalık bölmeli
     * 
     * FIXME: Burada sanırım bir hata yapıyoruz.
     * 
     */
    @Test()
    public void testdivide9() throws UnitException {
        System.out.println("divide9");
                
        Quantity q1 = Quantity.of(new BigDecimal(1.3), MassUnitSet.GRAM);
        Quantity q2 = new Quantity( new BigDecimal(7.21), MassUnitSet.CENTIGRAM);
        Quantity expResult = new Quantity( new BigDecimal("18.03051317614424"), MassUnitSet.GRAM);
        
        Quantity result = q1.divide(q2, MassUnitSet.GRAM);
        
        assertEquals(expResult, result);
        
    }
    
    /**
     * 
     * Ondalık bölmeli
     * 
     * FIXME: Burada sanırım bir hata yapıyoruz.
     * 
     */
    @Test()
    public void testdivide10() throws UnitException {
        System.out.println("divide9");
                
        Quantity q1 = Quantity.of(1.3, MassUnitSet.GRAM);
        Quantity q2 = Quantity.of(7.21, MassUnitSet.CENTIGRAM);
        Quantity expResult = Quantity.of("18.03051317614424", MassUnitSet.GRAM);
        
        Quantity result = q1.divide(q2, MassUnitSet.GRAM);
        
        assertEquals(expResult, result);
        
    }
    
}
