/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import javax.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

/**
 * Telve Komut aktarımı için route tanımları.
 *
 * @author Hakan Uygun
 */
@ContextName("telve")
public class CommandRoute extends RouteBuilder {


    @Inject
    private CommandProcessor commandProcessor;
    
    @Override
    public void configure() throws Exception {
        //Command endpointinden alınan mesajlar ilgili komut dinleyicisine aktarılıyor.       
        
        from("seda:command")
            .process(commandProcessor);
        

        for( String command : CommandRegistery.getCommands()){
            from("seda:"+command)
               .to(CommandRegistery.getEndpoint(command));
        }
        
        /*
         errorHandler(deadLetterChannel("mock:error"));
 
         from("direct:a")
         .choice()
         .when(header("foo").isEqualTo("bar"))
         .to("direct:b")
         .when(header("foo").isEqualTo("cheese"))
         .to("direct:c")
         .otherwise()
         .to("direct:d");
         */
    }

}
