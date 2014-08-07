/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.mutfak.telve;



import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Hakan Uygun
 */
public class TelveCoreModuleTest {
    
    @Inject
    TelveTestModule coreModule;
    
    @Test
    public void testSomeMethod() {
        
        System.out.println(coreModule.getTestMessage());
        
        // TODO review the generated test code and remove the default call to fail.
        Assert.fail("The test case is a prototype.");
    }
    
}
