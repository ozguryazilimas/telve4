/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

/**
 * Default Context'e yeni bir route ekleme testi
 * @author Hakan Uygun
 */
@ContextName("telve")
public class SimpleTestRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("direct:inbound").setHeader("context").constant("first").to("mock:outbound");
    }
    
}
