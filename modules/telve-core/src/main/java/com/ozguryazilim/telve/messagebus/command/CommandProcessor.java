/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haky
 */
@Dependent
public class CommandProcessor implements Processor{

    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);
    
    @Inject @ContextName("telve")
    private CamelContext context;
    
    //@Inject @ContextName("telve")
    private ProducerTemplate template;
    
    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Exchange Header : {}", exchange.getIn().getHeader("command") );
        
        String command = exchange.getIn().getHeader("command").toString();
               
        if( !CommandRegistery.isRegistered( command ) ){
            LOG.debug("Route To defaultCommandExecutor : {}", exchange);
            //Register olmadığı için default dinleyiciye gönderiyoruz...
            getTemplate().send("seda:" + Command.class.getName(), exchange);
        } else {
            getTemplate().send( CommandRegistery.getDispacher(command), exchange);
        }
    }
    
    private ProducerTemplate getTemplate(){
        if( template == null ){
            template = context.createProducerTemplate();
        }
        
        return template;
    }
    
}
