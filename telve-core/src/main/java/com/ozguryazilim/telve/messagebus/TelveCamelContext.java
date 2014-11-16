/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus;

import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.cdi.CdiCamelContext;
import org.apache.camel.cdi.ContextName;

/**
 * Telve Camel Context Bean
 * @author Hakan Uygun
 */
@ApplicationScoped
@ContextName("telve")
public class TelveCamelContext extends CdiCamelContext{
    
}
