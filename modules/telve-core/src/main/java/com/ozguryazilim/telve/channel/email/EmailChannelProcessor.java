/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.email;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.messages.Messages;
import java.util.Map;
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
        String template = exchange.getIn().getHeader("template") == null ? "" : exchange.getIn().getHeader("template").toString();
        
        String contactType = exchange.getIn().getHeader("contactType") == null ? "" : exchange.getIn().getHeader("contactType").toString();
        
        
        //Önce channel + messageClass + template + ContactType için bakıyoruz.
        LOG.debug("Looking Template for : {}", "channelTemplate.email." + messageClass + "." + template + "." + contactType);
        String templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.email." + messageClass + "." + template + "." + contactType );
        if( Strings.isNullOrEmpty(templateName) ){
            //Bulamadık key azaltıyoruz.
            LOG.debug("Looking Template for : {}", "channelTemplate.email." + messageClass + "." + template);
            templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.email." + messageClass + "." + template );
        }
        
        if( Strings.isNullOrEmpty(templateName) ){
            //Bulamadık key azaltıyoruz.
            LOG.debug("Looking Template for : {}", "channelTemplate.email." + messageClass);
            templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.email." + messageClass );
        }
        
        if( Strings.isNullOrEmpty(templateName) ){
            //Bulamadık default alıyoruz
            LOG.debug("Looking Template for : channelTemplate.email.GENERIC");
            templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.email.GENERIC" );
        }
        
        LOG.debug("Email Template Name : {}", templateName );
        
        exchange.getIn().setHeader("templateName", templateName);
        exchange.getIn().setHeader("messages", getMessages());
        exchange.getIn().setHeader("linkDomain", ConfigResolver.getPropertyValue("app.linkDomain"));
    }
    
    /**
     * Returns i18n message bundles
     * @return 
     */
    private Map<String,String> getMessages(){
        return Messages.getMessages();
    }
}
