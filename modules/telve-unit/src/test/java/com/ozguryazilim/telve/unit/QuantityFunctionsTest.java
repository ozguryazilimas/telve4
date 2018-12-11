/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import com.ozguryazilim.telve.unit.sets.MassUnitSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author oyas
 */
public class QuantityFunctionsTest {
    
    public QuantityFunctionsTest() {
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

    @Test
    public void testGroupBy() {
        
        List<QuantitativeAmount> quantities = new ArrayList<>();
        quantities.add(QuantitativeAmount.of(BigDecimal.ONE, MassUnitSet.GRAM));
        quantities.add(QuantitativeAmount.of(BigDecimal.TEN, MassUnitSet.GRAM));
        quantities.add(QuantitativeAmount.of(5, MassUnitSet.DECIGRAM));
        quantities.add(QuantitativeAmount.of(4, "ZAMAN:ISSAAT"));
        
        Map<UnitName,List<QuantitativeAmount>> result = quantities.stream().collect(Collectors.groupingBy(QuantitativeAmount::getUnitName));
        
        System.out.println(result);
        
        Assert.assertEquals(3, result.keySet().size());
        
    }
    
}
