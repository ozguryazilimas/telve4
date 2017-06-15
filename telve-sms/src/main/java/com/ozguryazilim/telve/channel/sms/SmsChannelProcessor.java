/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.sms;

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
 * Sms için gelen paketi messageClass'a göre zenginleştiriyoruz.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class SmsChannelProcessor implements Processor{
    private static final Logger LOG = LoggerFactory.getLogger(SmsChannelProcessor.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Exchange Header : {}", exchange.getIn().getHeader("messageClass") );
        
        String messageClass = exchange.getIn().getHeader("messageClass").toString();
        String template = exchange.getIn().getHeader("template") == null ? "" : exchange.getIn().getHeader("template").toString();
        
        String contactType = exchange.getIn().getHeader("contactType") == null ? "" : exchange.getIn().getHeader("contactType").toString();

        //Önce channel + messageClass + template + ContactType için bakıyoruz.
        LOG.debug("Looking templete for : {}", "channelTemplate.sms." + messageClass + "." + template + "." + contactType);
        String templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.sms." + messageClass + "." + template + "." + contactType );
        if( Strings.isNullOrEmpty(templateName) ){
            //Bulamadık key azaltıyoruz.
            LOG.debug("Looking templete for : {}", "channelTemplate.sms." + messageClass + "." + template);
            templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.sms." + messageClass + "." + template );
        }
        
        if( Strings.isNullOrEmpty(templateName) ){
            //Bulamadık key azaltıyoruz.
            LOG.debug("Looking templete for : {}", "channelTemplate.sms." + messageClass);
            templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.sms." + messageClass );
        }
        
        if( Strings.isNullOrEmpty(templateName) ){
            //Bulamadık default alıyoruz
            LOG.debug("Looking templete for : channelTemplate.sms.GENERIC");
            templateName = ConfigResolver.getProjectStageAwarePropertyValue("channelTemplate.sms.GENERIC" );
        }
        
        LOG.debug("SMS Template Name : {}", templateName );
        
        exchange.getIn().setHeader("templateName", templateName);
        exchange.getIn().setHeader("messages", getMessages());
    }
    
    /**
     * Returns i18n message bundles
     * @return 
     */
    private Map<String,String> getMessages(){
        return (new Messages()).getMessages();
    }
}
