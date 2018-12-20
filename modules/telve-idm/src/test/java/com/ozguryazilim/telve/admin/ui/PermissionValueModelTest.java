package com.ozguryazilim.telve.admin.ui;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author oyas
 */
public class PermissionValueModelTest {

    public PermissionValueModelTest() {
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
     * Test of clear method, of class PermissionValueModel.
     */
    @org.junit.Test
    public void testClear() {
        System.out.println("clear");

        System.out.println("setValue");
        String domain = "contact";
        String action = "insert";
        String value = "$owner";
        PermissionValueModel instance = new PermissionValueModel();
        instance.setValue(domain, action, value);

        String expResult = "owner";
        String result = instance.getValue(domain, action);
        assertEquals(expResult, result);

        //Sildikten sonra none gelmeli
        instance.clear();
        
        expResult = "NONE";
        result = instance.getValue(domain, action);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setValue method, of class PermissionValueModel.
     */
    @org.junit.Test
    public void testSetValue() {
        System.out.println("setValue");
        String domain = "contact";
        String action = "insert";
        String value = "$owner";
        PermissionValueModel instance = new PermissionValueModel();
        instance.setValue(domain, action, value);

        String expResult = "owner";
        String result = instance.getValue(domain, action);
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class PermissionValueModel.
     *
     * Olmayan değer için none gelmeli
     */
    @org.junit.Test
    public void testGetValue() {
        System.out.println("getValue");
        String domain = "contact";
        String action = "insert";
        PermissionValueModel instance = new PermissionValueModel();
        String expResult = "NONE";
        String result = instance.getValue(domain, action);
        assertEquals(expResult, result);

    }

    /**
     * Test of setPermissionValues method, of class PermissionValueModel.
     */
    @org.junit.Test
    public void testSetPermissionValues_String() {
        System.out.println("setPermissionValues");
        String permStr = "contact:select,insert,update:$group";
        PermissionValueModel instance = new PermissionValueModel();
        instance.setPermissionValues(permStr);

        String expResult = "group";
        String result = instance.getValue("contact", "insert");
        assertEquals(expResult, result);

        expResult = "NONE";
        result = instance.getValue("contact", "delete");
        assertEquals(expResult, result);
    }

    /**
     * Test of getPermissionValues method, of class PermissionValueModel.
     */
    @org.junit.Test
    public void testGetPermissionValues() {
        System.out.println("getPermissionValues");
        PermissionValueModel instance = new PermissionValueModel();
        List<String> expResult = new ArrayList<>();
        
        expResult.add("contact:insert:*");
        expResult.add("contact:select,update:$group");
        expResult.add("contact:delete:$owner");
        
        expResult.forEach((s) -> {
            instance.setPermissionValues(s);
        });
        
        instance.setValue("contact", "insert", "ALL");
        
        List<String> result = instance.getPermissionValues();
        
        expResult.forEach((s) -> {
            assertTrue(result.contains(s));
        });
        
        System.out.println(expResult);
        System.out.println(result);
        
    }
    
    /**
     * Test of getPermissionValues method, of class PermissionValueModel.
     */
    @org.junit.Test
    public void testGetPermissionValues2() {
        System.out.println("getPermissionValues2");
        PermissionValueModel instance = new PermissionValueModel();
        List<String> expResult = new ArrayList<>();
        
        expResult.add("contact:insert:*");
        expResult.add("contact:select,update:$group");
        expResult.add("contact:delete:$owner");
        
        expResult.forEach((s) -> {
            instance.setPermissionValues(s);
        });
        
        instance.setValue("contact", "insert", "NONE");
        
        List<String> result = instance.getPermissionValues();
        
        
        assertFalse(result.contains("contact:insert:*"));
        
        
        System.out.println(expResult);
        System.out.println(result);
        
    }

}
