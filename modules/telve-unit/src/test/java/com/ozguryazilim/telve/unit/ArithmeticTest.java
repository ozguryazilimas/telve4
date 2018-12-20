package com.ozguryazilim.telve.unit;

import com.ozguryazilim.telve.unit.sets.MassUnitSet;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.Test;

/**
 * Unit ile ilgili aritmetik işlemler için Test
 * 
 * Presicion ve Scale ve Round
 * 
 * @author oyas
 */
public class ArithmeticTest {
   
    
    /**
     * Bu test hata verecek
     */
    @Test( expected = ArithmeticException.class)
    public void testDivide1(){
        System.out.println("Divide1");
        
        BigDecimal a = new BigDecimal(1.3);
        BigDecimal b = new BigDecimal(3.71);
        
        BigDecimal r = a.divide(b);
                
        System.out.println(r);
        
    }
    
    /**
     * Bu test hata verecek
     */
    @Test()
    public void testDivide2(){
        System.out.println("Divide2");
        
        BigDecimal a = new BigDecimal(1.3);
        BigDecimal b = new BigDecimal(3.71);
        
        System.out.println(a.divide(b, RoundingMode.UP));
        System.out.println(a.divide(b, RoundingMode.HALF_UP));
        System.out.println(a.divide(b, RoundingMode.HALF_EVEN));
        System.out.println(a.divide(b, RoundingMode.HALF_DOWN));
        
        
        System.out.println(a.divide(b, 2, RoundingMode.UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_EVEN));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_DOWN));
        
        
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        System.out.println(a.divide(b, MathContext.DECIMAL64));
        System.out.println(a.divide(b, MathContext.DECIMAL128));
        
        BigDecimal c = a.setScale(4, RoundingMode.HALF_UP);
        
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(c.divide(b, MathContext.DECIMAL32));
        System.out.println(c.divide(b, MathContext.DECIMAL64));
        System.out.println(c.divide(b, MathContext.DECIMAL128));
    }
    
    @Test()
    public void testDivide3(){
        System.out.println("Divide2");
        
        BigDecimal a = new BigDecimal(1.3, MathContext.DECIMAL32);
        BigDecimal b = new BigDecimal(3.71, MathContext.DECIMAL32);
        
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        
        
        System.out.println(a.divide(b, 2, RoundingMode.UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_EVEN));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_DOWN));
        
        
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        System.out.println(a.divide(b, MathContext.DECIMAL64));
        System.out.println(a.divide(b, MathContext.DECIMAL128));
        
        BigDecimal c = a.setScale(4, RoundingMode.HALF_UP);
        
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(c.divide(b, MathContext.DECIMAL32));
        System.out.println(c.divide(b, MathContext.DECIMAL64));
        System.out.println(c.divide(b, MathContext.DECIMAL128));
    }
    
    
    @Test()
    public void testDivide4(){
        System.out.println("Divide2");
        
        BigDecimal a = new BigDecimal(22);
        BigDecimal b = new BigDecimal(7);
        
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        
        
        System.out.println(a.divide(b, 2, RoundingMode.UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_EVEN));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_DOWN));
        
        System.out.println("/");
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        System.out.println(a.divide(b, MathContext.DECIMAL64));
        System.out.println(a.divide(b, MathContext.DECIMAL128));
        
        System.out.println("+");
        System.out.println(a.add(b, MathContext.DECIMAL32));
        System.out.println(a.add(b, MathContext.DECIMAL64));
        System.out.println(a.add(b, MathContext.DECIMAL128));
        
        System.out.println("*");
        System.out.println(a.multiply(b, MathContext.DECIMAL32));
        System.out.println(a.multiply(b, MathContext.DECIMAL64));
        System.out.println(a.multiply(b, MathContext.DECIMAL128));
        
        System.out.println("-");
        System.out.println(a.subtract(b, MathContext.DECIMAL32));
        System.out.println(a.subtract(b, MathContext.DECIMAL64));
        System.out.println(a.subtract(b, MathContext.DECIMAL128));
        
        
        System.out.println("Other");
        BigDecimal c = a.setScale(4, RoundingMode.HALF_UP);
        
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(c.divide(b, MathContext.DECIMAL32));
        System.out.println(c.divide(b, MathContext.DECIMAL64));
        System.out.println(c.divide(b, MathContext.DECIMAL128));
    }
    
    
    @Test()
    public void testDivide5(){
        System.out.println("Divide5");
        
        BigDecimal a = new BigDecimal(12422.23);
        BigDecimal b = new BigDecimal(317.37);
        
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        
        
        System.out.println(a.divide(b, 2, RoundingMode.UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_EVEN));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_DOWN));
        
        System.out.println("/");
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        System.out.println(a.divide(b, MathContext.DECIMAL64));
        System.out.println(a.divide(b, MathContext.DECIMAL128));
        
        System.out.println("+");
        System.out.println(a.add(b, MathContext.DECIMAL32));
        System.out.println(a.add(b, MathContext.DECIMAL64));
        System.out.println(a.add(b, MathContext.DECIMAL128));
        
        System.out.println("*");
        System.out.println(a.multiply(b, MathContext.DECIMAL32));
        System.out.println(a.multiply(b, MathContext.DECIMAL64));
        System.out.println(a.multiply(b, MathContext.DECIMAL128));
        
        
        System.out.println("-");
        System.out.println(a.subtract(b, MathContext.DECIMAL32));
        System.out.println(a.subtract(b, MathContext.DECIMAL64));
        System.out.println(a.subtract(b, MathContext.DECIMAL128));

    }
    
    
    
    @Test()
    public void testDivide6(){
        System.out.println("Divide6");
        
        BigDecimal a = new BigDecimal(3412422.231456);
        BigDecimal b = new BigDecimal(32317.37);
        
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        
        
        System.out.println(a.divide(b, 2, RoundingMode.UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_UP));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_EVEN));
        System.out.println(a.divide(b, 2, RoundingMode.HALF_DOWN));
        
        System.out.println("/");
        System.out.println(a.divide(b, new MathContext(7, RoundingMode.HALF_UP)));
        System.out.println(a.divide(b, MathContext.DECIMAL32));
        System.out.println(a.divide(b, MathContext.DECIMAL64));
        System.out.println(a.divide(b, MathContext.DECIMAL128));
        
        System.out.println("+");
        System.out.println(a.add(b, new MathContext(7, RoundingMode.HALF_UP)));
        System.out.println(a.add(b, MathContext.DECIMAL32));
        System.out.println(a.add(b, MathContext.DECIMAL64));
        System.out.println(a.add(b, MathContext.DECIMAL128));
        
        System.out.println("*");
        System.out.println(a.multiply(b, new MathContext(7, RoundingMode.HALF_UP)));
        System.out.println(a.multiply(b, MathContext.DECIMAL32));
        System.out.println(a.multiply(b, MathContext.DECIMAL64));
        System.out.println(a.multiply(b, MathContext.DECIMAL128));
        
        
        System.out.println("-");
        System.out.println(a.subtract(b, new MathContext(7, RoundingMode.HALF_UP)));
        System.out.println(a.subtract(b, MathContext.DECIMAL32));
        System.out.println(a.subtract(b, MathContext.DECIMAL64));
        System.out.println(a.subtract(b, MathContext.DECIMAL128));

    }
    
    @Test
    public void testDividQuantiy(){
        
        QuantitativeAmount q = Quantities.of(new BigDecimal(22), MassUnitSet.GRAM);
        
        QuantitativeAmount r = Quantities.divide(q, new BigDecimal(7));
        
        System.out.println(r);
        
        
    }
    
    @Test
    public void testDividQuantiy2() throws UnitException{
        
        QuantitativeAmount q1 = Quantities.of(new BigDecimal(22), MassUnitSet.GRAM);
        QuantitativeAmount q2 = Quantities.of(new BigDecimal(7), MassUnitSet.GRAM);
        
        QuantitativeAmount r1 = Quantities.divide( q1, q2 );
        QuantitativeAmount r2 = Quantities.divide( q1, new BigDecimal(7));
        
        System.out.println(r1);
        System.out.println(r2);
        
        
    }
    
}
