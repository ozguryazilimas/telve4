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

    
    private static final QuantitativeAmount TEN_GRAM = new QuantitativeAmount(BigDecimal.TEN, MassUnitSet.GRAM);
    
    /**
     * Test of getAmount method, of class QuantitativeAmount.
     */
    @Test
    public void testGetAmount() {
        System.out.println("getAmount");
        QuantitativeAmount instance = new QuantitativeAmount(BigDecimal.ONE, MassUnitSet.GRAM);
        BigDecimal expResult = BigDecimal.ONE;
        BigDecimal result = instance.getAmount();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getUnitName method, of class QuantitativeAmount.
     */
    @Test
    public void testGetUnitName() {
        System.out.println("getUnitName");
        QuantitativeAmount instance = new QuantitativeAmount(BigDecimal.ONE, MassUnitSet.GRAM);
        UnitName expResult = MassUnitSet.GRAM;
        UnitName result = instance.getUnitName();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_BigDecimal_UnitName() {
        System.out.println("of");
        BigDecimal a = BigDecimal.TEN;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_BigDecimal_String() {
        System.out.println("of");
        BigDecimal a = BigDecimal.TEN;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_long_String() {
        System.out.println("of");
        long a = 10L;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_int_String() {
        System.out.println("of");
        int a = 10;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_double_String() {
        System.out.println("of");
        double a = 10.0;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_float_String() {
        System.out.println("of");
        float a = 10.0F;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_String_String() {
        System.out.println("of");
        String a = "10";
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Long_String() {
        System.out.println("of");
        Long a = 10l;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Integer_String() {
        System.out.println("of");
        Integer a = 10;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Double_String() {
        System.out.println("of");
        Double a = 10.0;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Float_String() {
        System.out.println("of");
        Float a = 10.0F;
        String un = "MASS:GRAM";
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_long_UnitName() {
        System.out.println("of");
        long a = 10L;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_int_UnitName() {
        System.out.println("of");
        int a = 10;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_double_UnitName() {
        System.out.println("of");
        double a = 10.0;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_float_UnitName() {
        System.out.println("of");
        float a = 10.0F;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_String_UnitName() {
        System.out.println("of");
        String a = "10";
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Long_UnitName() {
        System.out.println("of");
        Long a = 10l;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Integer_UnitName() {
        System.out.println("of");
        Integer a = 10;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Double_UnitName() {
        System.out.println("of");
        Double a = 10.0;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of of method, of class QuantitativeAmount.
     */
    @Test
    public void testOf_Float_UnitName() {
        System.out.println("of");
        Float a = 10.0F;
        UnitName un = MassUnitSet.GRAM;
        QuantitativeAmount expResult = TEN_GRAM;
        QuantitativeAmount result = QuantitativeAmount.of(a, un);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of toString method, of class QuantitativeAmount.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        QuantitativeAmount instance = TEN_GRAM;
        String expResult = "MASS:GRAM(10)";
        String result = instance.toString();
        assertEquals(expResult, result);
        
    }


    /**
     * Test of convert method, of class QuantitativeAmount.
     */
    @Test
    public void testConvert() throws Exception {
        System.out.println("convert");
        UnitName unitName = MassUnitSet.DECIGRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(100, MassUnitSet.DECIGRAM);
        QuantitativeAmount result = instance.convert(unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of add method, of class QuantitativeAmount.
     */
    @Test
    public void testAdd_BigDecimal() {
        System.out.println("add");
        BigDecimal amount = BigDecimal.TEN;
        QuantitativeAmount instance = QuantitativeAmount.of(1, MassUnitSet.GRAM);
        QuantitativeAmount expResult = QuantitativeAmount.of(11, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.add(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of add method, of class QuantitativeAmount.
     */
    @Test
    public void testAdd_Quantity() throws Exception {
        System.out.println("add");
        QuantitativeAmount that = TEN_GRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(20, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.add(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of add method, of class QuantitativeAmount.
     */
    @Test
    public void testAdd_Quantity_UnitName() throws Exception {
        System.out.println("add");
        QuantitativeAmount that = TEN_GRAM;
        UnitName unitName = MassUnitSet.DECIGRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(200, MassUnitSet.DECIGRAM);
        QuantitativeAmount result = instance.add(that, unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of subtract method, of class QuantitativeAmount.
     */
    @Test
    public void testSubtract_BigDecimal() {
        System.out.println("subtract");
        BigDecimal amount = BigDecimal.ONE;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(9, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.subtract(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of subtract method, of class QuantitativeAmount.
     */
    @Test
    public void testSubtract_Quantity() throws Exception {
        System.out.println("subtract");
        QuantitativeAmount that = QuantitativeAmount.of(1, MassUnitSet.GRAM);
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(9, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.subtract(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of subtract method, of class QuantitativeAmount.
     */
    @Test
    public void testSubtract_Quantity_UnitName() throws Exception {
        System.out.println("subtract");
        QuantitativeAmount that = QuantitativeAmount.of(1, MassUnitSet.GRAM);
        UnitName unitName = MassUnitSet.DECIGRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(90, MassUnitSet.DECIGRAM);
        QuantitativeAmount result = instance.subtract(that, unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of multiply method, of class QuantitativeAmount.
     */
    @Test
    public void testMultiply_BigDecimal() {
        System.out.println("multiply");
        BigDecimal amount = BigDecimal.TEN;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(100, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.multiply(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of multiply method, of class QuantitativeAmount.
     */
    @Test
    public void testMultiply_Quantity() throws Exception {
        System.out.println("multiply");
        QuantitativeAmount that = TEN_GRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(100, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.multiply(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of multiply method, of class QuantitativeAmount.
     */
    @Test
    public void testMultiply_Quantity_UnitName() throws Exception {
        System.out.println("multiply");
        QuantitativeAmount that = TEN_GRAM;
        UnitName unitName = MassUnitSet.DECIGRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(10000, MassUnitSet.DECIGRAM);;
        QuantitativeAmount result = instance.multiply(that, unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of divide method, of class QuantitativeAmount.
     */
    @Test
    public void testDivide_BigDecimal() {
        System.out.println("divide");
        BigDecimal amount = new BigDecimal(2.5);
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(4, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.divide(amount);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of divide method, of class QuantitativeAmount.
     */
    @Test
    public void testDivide_Quantity() throws Exception {
        System.out.println("divide");
        QuantitativeAmount that = QuantitativeAmount.of(2.5, MassUnitSet.GRAM);
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(4, MassUnitSet.GRAM);
        QuantitativeAmount result = instance.divide(that);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of divide method, of class QuantitativeAmount.
     * //TODO: Bu test bir daha incelenmeli hesap hatası var gibi
     */
    @Test
    public void testDivide_Quantity_UnitName() throws Exception {
        System.out.println("divide");
        QuantitativeAmount that = QuantitativeAmount.of(2.5, MassUnitSet.GRAM);;
        UnitName unitName = MassUnitSet.DECIGRAM;
        QuantitativeAmount instance = TEN_GRAM;
        QuantitativeAmount expResult = QuantitativeAmount.of(4, MassUnitSet.DECIGRAM);
        QuantitativeAmount result = instance.divide(that, unitName);
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
                
        QuantitativeAmount q1 = QuantitativeAmount.of(22, MassUnitSet.GRAM);
        QuantitativeAmount q2 = QuantitativeAmount.of(7, MassUnitSet.GRAM);
        QuantitativeAmount expResult = QuantitativeAmount.of("3.142857142857143", MassUnitSet.GRAM);
        
        QuantitativeAmount result = q1.divide(q2, MassUnitSet.GRAM);
        
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
                
        QuantitativeAmount q1 = QuantitativeAmount.of(new BigDecimal(1.3), MassUnitSet.GRAM);
        QuantitativeAmount q2 = new QuantitativeAmount( new BigDecimal(7.21), MassUnitSet.CENTIGRAM);
        QuantitativeAmount expResult = new QuantitativeAmount( new BigDecimal("18.03051317614424"), MassUnitSet.GRAM);
        
        QuantitativeAmount result = q1.divide(q2, MassUnitSet.GRAM);
        
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
                
        QuantitativeAmount q1 = QuantitativeAmount.of(1.3, MassUnitSet.GRAM);
        QuantitativeAmount q2 = QuantitativeAmount.of(7.21, MassUnitSet.CENTIGRAM);
        QuantitativeAmount expResult = QuantitativeAmount.of("18.03051317614424", MassUnitSet.GRAM);
        
        QuantitativeAmount result = q1.divide(q2, MassUnitSet.GRAM);
        
        assertEquals(expResult, result);
        
    }
    
}
