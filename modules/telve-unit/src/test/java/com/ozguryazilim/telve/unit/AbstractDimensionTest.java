package com.ozguryazilim.telve.unit;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author oyas
 */
public class AbstractDimensionTest {
    
    public AbstractDimensionTest() {
    }
    
    private static final String DIMENSION_NAME = "MASS";
    
    public static final UnitName MILLIGRAM = new UnitName(DIMENSION_NAME, "MILLIGRAM");
    public static final UnitName CENTIGRAM = new UnitName(DIMENSION_NAME, "CENTIGRAM");
    public static final UnitName DECIGRAM = new UnitName(DIMENSION_NAME, "DECIGRAM");
    public static final UnitName GRAM = new UnitName(DIMENSION_NAME, "GRAM");
    public static final UnitName DECAGRAM = new UnitName(DIMENSION_NAME, "DECAGRAM");
    public static final UnitName HECTOGRAM = new UnitName(DIMENSION_NAME, "HECTOGRAM");
    public static final UnitName KILOGRAM = new UnitName(DIMENSION_NAME, "KILOGRAM");
    
    public final static Unit MILLIGRAM_UNIT = new Unit(MILLIGRAM, new QuantitativeAmount(BigDecimal.ONE, MILLIGRAM));
    public final static Unit CENTIGRAM_UNIT = new Unit(CENTIGRAM, new QuantitativeAmount(BigDecimal.TEN, MILLIGRAM));
    public final static Unit DECIGRAM_UNIT = new Unit(DECIGRAM, new QuantitativeAmount(BigDecimal.TEN, CENTIGRAM));
    public final static Unit GRAM_UNIT = new Unit(GRAM, new QuantitativeAmount(BigDecimal.TEN, DECIGRAM));
    public final static Unit DECAGRAM_UNIT = new Unit(DECAGRAM, new QuantitativeAmount(new BigDecimal(1000), CENTIGRAM));
    public final static Unit HECTOGRAM_UNIT = new Unit(HECTOGRAM, new QuantitativeAmount(BigDecimal.TEN, DECAGRAM));
    public final static Unit KILOGRAM_UNIT = new Unit(KILOGRAM, new QuantitativeAmount(BigDecimal.TEN, HECTOGRAM));
    
    public AbstractDimensionImpl testDimension;
    
    @Before
    public void setUp() {
        testDimension = new AbstractDimensionImpl();
        testDimension.addUnit(MILLIGRAM_UNIT);
        testDimension.addUnit(CENTIGRAM_UNIT);
        testDimension.addUnit(DECIGRAM_UNIT);
        testDimension.addUnit(GRAM_UNIT);
        testDimension.addUnit(DECAGRAM_UNIT);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDimensionName method, of class AbstractUnitSet.
     */
    @Test
    public void testGetDimensionName() {
        System.out.println("getDimensionName");
        AbstractUnitSet instance = new AbstractDimensionImpl();
        String expResult = DIMENSION_NAME;
        String result = instance.getDimensionName();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of addBaseUnit method, of class AbstractUnitSet.
     */
    @Test
    public void testAddBaseUnit() throws UnitException {
        System.out.println("addBaseUnit");
        String unitName = "MILLIGRAM";
        AbstractUnitSet instance = new AbstractDimensionImpl();
        instance.addBaseUnit(unitName);
        System.out.println( instance.getUnitChain(MILLIGRAM_UNIT));
    }

    /**
     * Test of addUnit method, of class AbstractUnitSet.
     */
    @Test
    public void testAddUnit_String_Quantity() throws UnitException {
        System.out.println("addUnit");
        String unitName = "CENTIGRAM";
        QuantitativeAmount base = new QuantitativeAmount(BigDecimal.TEN, MILLIGRAM);
        AbstractUnitSet instance = new AbstractDimensionImpl();
        instance.addBaseUnit("MILLIGRAM");
        instance.addUnit(unitName, base);
        
        System.out.println( instance.getUnitChain(CENTIGRAM_UNIT));
    }

    /**
     * Test of addUnit method, of class AbstractUnitSet.
     */
    @Test
    public void testAddUnit_Unit() throws UnitException {
        System.out.println("addUnit");
        Unit unit = MILLIGRAM_UNIT;
        AbstractUnitSet instance = new AbstractDimensionImpl();
        instance.addUnit(unit);
        
        System.out.println( instance.getUnitChain(MILLIGRAM_UNIT));
    }

    /**
     * Test of getUnit method, of class AbstractUnitSet.
     */
    @Test
    public void testGetUnit() {
        System.out.println("getUnit");
        UnitName unitName = MILLIGRAM;
        AbstractUnitSet instance = testDimension;
        Unit expResult = MILLIGRAM_UNIT;
        Unit result = instance.getUnit(unitName);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getUnitChain method, of class AbstractUnitSet.
     */
    @Test
    public void testGetUnitChain1() throws UnitException {
        System.out.println("getUnitChain");
        Unit from = GRAM_UNIT;
        AbstractUnitSet instance = testDimension;
        List<Unit> expResult = Arrays.asList( new Unit[]{GRAM_UNIT, DECIGRAM_UNIT, CENTIGRAM_UNIT, MILLIGRAM_UNIT});
        List<Unit> result = instance.getUnitChain(from);
        assertEquals(expResult, result);
    }

    
    @Test
    public void testGetUnitChain2() throws UnitException {
        System.out.println("getUnitChain2");
        Unit from = MILLIGRAM_UNIT;
        AbstractUnitSet instance = testDimension;
        
        List<Unit> expResult = Arrays.asList( new Unit[]{MILLIGRAM_UNIT});
        List<Unit> result = instance.getUnitChain(from);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Olmayan bir from gelirse Exception
     */
    @Test(expected = UnitException.class)
    public void testGetUnitChain3() throws UnitException {
        System.out.println("getUnitChain3");
        Unit from = KILOGRAM_UNIT;
        AbstractUnitSet instance = testDimension;
        instance.getUnitChain(from);
    }
    
    /**
     * Zincir bozuksa?
     */
    @Test(expected = UnitException.class)
    public void testGetUnitChain4() throws UnitException {
        System.out.println("getUnitChain4");
        Unit from = KILOGRAM_UNIT;
        AbstractUnitSet instance = new AbstractDimensionImpl();
        
        instance.addUnit(KILOGRAM_UNIT);
        
        instance.getUnitChain(from);
        
    }
    
    
    @Test
    public void testGetFactors() throws UnitException {
        System.out.println("getFactors");
        
        
        AbstractUnitSet instance = testDimension;
        
        BigDecimal[] result = instance.getFactors(MILLIGRAM, GRAM);
        //Carpan
        assertEquals(BigDecimal.ONE, result[0]);
        //Bolen
        assertEquals(new BigDecimal(1000), result[1]);
        
        
        result = instance.getFactors(DECIGRAM, GRAM);
        //Carpan
        assertEquals(BigDecimal.ONE, result[0]);
        //Bolen
        assertEquals(BigDecimal.TEN, result[1]);
        
        
        result = instance.getFactors(CENTIGRAM, GRAM);
        //Carpan
        assertEquals(BigDecimal.ONE, result[0]);
        //Bolen
        assertEquals(new BigDecimal(100), result[1]);
        
        
        result = instance.getFactors(GRAM, DECIGRAM);
        //Carpan
        assertEquals(BigDecimal.TEN, result[0]);
        //Bolen
        assertEquals(BigDecimal.ONE, result[1]);
        
        //Doğrusal olamayan bir yolu test edelim. DECAGRAM tanımı GRAM ve DECIGRAM'ı atlayıp doğrudan CENTIGRAM üzerinden tanıtıldı
        result = instance.getFactors(DECAGRAM, GRAM);
        //Carpan
        assertEquals(new BigDecimal(1000), result[0]);
        //Bolen
        assertEquals(new BigDecimal(100), result[1]);
        
        
        //assertEquals(expResult, result);
        
    }
    
    public class AbstractDimensionImpl extends AbstractUnitSet {

        public String getDimensionName() {
            return DIMENSION_NAME;
        }
    }
    
}
