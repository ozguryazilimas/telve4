package com.ozguryazilim.telve.channel.sms;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

/**
 * SMS Channel için route tanımı
 * 
 * @author Hakan Uygun
 */
@Dependent @ContextName("telve")
public class SmsChannelRoute extends RouteBuilder{
    
    @Inject
    private SmsChannelProcessor channelProcessor;
    
    @Override
    public void configure() throws Exception {
        //Gelen mesajın hangi template'i kullanacağı processor ile bulunup header.templateName'e konuyor
        from("seda:smsChannel")
           .process(channelProcessor)
           .recipientList(simple("${header.templateName}"))
           .to("bean:smsChannelDispacher");
    }
}
