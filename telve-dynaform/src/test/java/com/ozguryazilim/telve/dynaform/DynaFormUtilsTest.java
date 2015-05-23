/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import com.ozguryazilim.telve.dynaform.model.DynaForm;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author haky
 */
public class DynaFormUtilsTest {
    
    /**
     * Test of toJson method, of class DynaFormUtils.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");
        Map<String, Object> valueMap = new HashMap<>();
        
        valueMap.put("test:1:1", "Deneme");
        valueMap.put("test:1:2", 5);
        valueMap.put("test:1:3", new Date());
        valueMap.put("test:1:4", 11l);
        valueMap.put("test:1:5", BigDecimal.TEN);
        valueMap.put("test:1:6", Boolean.TRUE);
        valueMap.put("test:1:7", 3.2d);
        
        String expResult = "";
        String result = DynaFormUtils.fieldValuestoJson(valueMap);
        //assertEquals(expResult, result);
        System.out.println(result);
    }
    
    @Test
    public void testFromJson2() {
        System.out.println("fromJson2");
        Map<String, Serializable> valueMap = new HashMap<>();
        
        String expResult = "";
        
        TestFormBuilder fb = new TestFormBuilder();
        
        DynaForm form = fb.buildTestForm();
        
        
        Map<String,Object> result = DynaFormUtils.fieldValuesfromJson("{\"fld1\":\"test:1:5\"}", form);
        for( Map.Entry<String,Object> e : result.entrySet()){
            System.out.print(e);
            System.out.println("    " + e.getValue().getClass().getName());
        }
        System.out.println(result);
    }
    
}
