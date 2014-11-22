/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author haky
 */
public class KahveCriteriaTest {
    
    

    /**
     * Test of create method, of class KahveCriteria.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        List<String> expResult = new ArrayList<>();
        
        expResult.add("HAKAN::KEY");
        expResult.add("admin::KEY");
        expResult.add("merkez::KEY");
        expResult.add("KEY");
        
        KahveCriteria result = 
                KahveCriteria.create()
                    .addScope("admin")
                    .addScope("merkez")
                .addKey("KEY")
                .addDefaultValue(new KahveEntry("BİŞİ"));
        
        
        Assert.assertEquals(expResult, result.getKeys("HAKAN"));
        
        System.out.println(result);
        
        System.out.println(result.getKeys( "HAKAN" ));
        
    }
    
    /**
     * Test of create method, of class KahveCriteria.
     */
    @Test
    public void testExculudeUserScope() {
        System.out.println("create");
        List<String> expResult = new ArrayList<>();
        
        expResult.add("admin::KEY");
        expResult.add("merkez::KEY");
        expResult.add("KEY");
        
        KahveCriteria result = 
                KahveCriteria.create()
                    .addScope("admin")
                    .addScope("merkez")
                .addKey("KEY")
                .addDefaultValue(new KahveEntry("BİŞİ"));
        
        
        Assert.assertEquals(expResult, result.getKeys());
        
        System.out.println(result);
        
        System.out.println(result.getKeys( "HAKAN" ));
        
    }
    
    
    /**
     * Test of create method, of class KahveCriteria.
     */
    @Test
    public void testHasDefaultValue() {
        System.out.println("HasDefaultValue");
        
        KahveCriteria result = 
                KahveCriteria.create()
                    .addScope("admin")
                    .addScope("merkez")
                .addKey("KEY");
                
        
        
        Assert.assertFalse(result.hasDefaultValue());
        
        result.addDefaultValue(new KahveEntry("BİŞİ"));
        Assert.assertTrue(result.hasDefaultValue());

        
    }

    /**
     * Test of create method, of class KahveCriteria.
     */
    @Test
    public void testDefaultValue() {
        System.out.println("DefaultValue");
        
        KahveCriteria result = 
                KahveCriteria.create()
                    .addScope("admin")
                    .addScope("merkez")
                .addKey("KEY");
                
        result.addDefaultValue(new KahveEntry("BİŞİ"));
        
        Assert.assertTrue(result.hasDefaultValue());

        Assert.assertEquals( new KahveEntry("BİŞİ"), result.getDefaultValue() );
        Assert.assertEquals( "HAKAN::KEY", result.getScopeKey("HAKAN") );
        
    }
    
    
    /**
     * Test of create method, of class KahveCriteria.
     */
    @Test
    public void testDefaultValueKAhveKey() {
        System.out.println("DefaultValueKAhveKey");
        
        KahveCriteria result = 
                KahveCriteria.create()
                    .addScope("admin")
                    .addScope("merkez")
                .addKey(TestKahveKey.Test1);
                
        
        Assert.assertTrue(result.hasDefaultValue());

        Assert.assertEquals( new KahveEntry(TestKahveKey.Test1), result.getDefaultValue() );
        Assert.assertEquals( "HAKAN::TEST-KEY1", result.getScopeKey("HAKAN") );
        
    }
    
}
