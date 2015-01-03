/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.notify;

import javax.enterprise.context.Dependent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notify için gelen paketi messageClass'a göre zenginleştiriyoruz.
 * @author Hakan Uygun
 */
@Dependent
public class NotifyChannelProcessor implements Processor{
    private static final Logger LOG = LoggerFactory.getLogger(NotifyChannelProcessor.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Exchange Header : {}", exchange.getIn().getHeader("messageClass") );
        
        String messageClass = exchange.getIn().getHeader("messageClass").toString();
        String templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.notify." + messageClass);
        
        LOG.debug("Notify Template Name : {}", templateName );
        
        exchange.getIn().setHeader("templateName", templateName);
    }
    
    
}
