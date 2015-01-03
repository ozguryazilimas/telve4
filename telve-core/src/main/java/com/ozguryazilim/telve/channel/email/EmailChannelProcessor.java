/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.email;

import javax.enterprise.context.Dependent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EMail için gelen paketi messageClass'a göre zenginleştiriyoruz.
 * @author Hakan Uygun
 */
@Dependent
public class EmailChannelProcessor implements Processor{
    private static final Logger LOG = LoggerFactory.getLogger(EmailChannelProcessor.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Exchange Header : {}", exchange.getIn().getHeader("messageClass") );
        
        String messageClass = exchange.getIn().getHeader("messageClass").toString();
        String templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.email." + messageClass);
        
        LOG.debug("Email Template Name : {}", templateName );
        
        exchange.getIn().setHeader("templateName", templateName);
    }
}
