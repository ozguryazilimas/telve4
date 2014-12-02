/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import com.ozguryazilim.telve.commands.ExecutionLogClearCommand;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author haky
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandRegisteryTest {
    
    @BeforeClass
    public static void beforeTest(){
        String name = Command.class.getName();
        String endpoint = "bean:Test";
        CommandRegistery.register(name, endpoint, endpoint);
        
        CommandRegistery.register(AbstractJBatchCommand.class.getName(), "seda:JBatchCommandExecutor", "bean:JBatchCommandExecutor");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of register method, of class CommandRegistery.
     */
    //@Test
    public void testRegister() {
        System.out.println("register");
        String name = Command.class.getName();
        String endpoint = "bean:Test";
        CommandRegistery.register(name, endpoint, endpoint);
    
        
        assertEquals(endpoint, CommandRegistery.getEndpoint(Command.class.getName()));
    }

    /**
     * Test of getEndpoints method, of class CommandRegistery.
     */
    @Test
    public void testGetEndpoints() {
        System.out.println("getEndpoints");
        List<String> expResult = new ArrayList();
        expResult.add("bean:Test");
        List<String> result = CommandRegistery.getEndpoints();
        assertFalse(result.isEmpty());
    }

    /**
     * Test of getCommands method, of class CommandRegistery.
     */
    @Test
    public void testGetCommands() {
        System.out.println("getCommands");
        List<String> result = CommandRegistery.getCommands();
        assertFalse(result.isEmpty());
    }

    /**
     * Test of isRegistered method, of class CommandRegistery.
     */
    @Test
    public void testIsRegistered() {
        System.out.println("isRegistered");
        
        boolean result = CommandRegistery.isRegistered("HatalıArama");
        Assert.assertFalse(result);
        
        result = CommandRegistery.isRegistered(Command.class.getName());
        Assert.assertTrue(result);
    }

    /**
     * Test of getEndpoint method, of class CommandRegistery.
     */
    @Test
    public void testGetEndpoint() {
        System.out.println("getEndpoint");
        String name = Command.class.getName();
        String expResult = "bean:Test";
        String result = CommandRegistery.getEndpoint(name);
        assertEquals(expResult, result);
    }
    
    
    /**
     * Test of getEndpoint method, of class CommandRegistery.
     */
    @Test
    public void testAbstractCommand() {
        System.out.println("testAbstractCommand");
        System.out.println(CommandRegistery.getCommands());
        
        String name = ExecutionLogClearCommand.class.getName();

        boolean result = CommandRegistery.isRegistered(name);
        
        System.out.println(CommandRegistery.getCommands());
        Assert.assertTrue(result);
        
        result = CommandRegistery.isRegistered(LogCommand.class.getName());
        Assert.assertFalse(result);
        
        
    }
    
}
