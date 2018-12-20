package com.ozguryazilim.telve.channel.notify;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

/**
 * NaotifyChannel için Camel Route tanımı
 * @author Hakan Uygun
 */
@Dependent @ContextName("telve")
public class NotifyChannelRoute extends RouteBuilder{
    
    @Inject
    private NotifyChannelProcessor channelProcessor;
    
    @Override
    public void configure() throws Exception {
        //Gelen mesajın hangi template'i kullanacağı processor ile bulunup header.templateName'e konuyor
        from("seda:notifyChannel")
           .process(channelProcessor)
           .recipientList(simple("${header.templateName}"))
           .to("bean:notifyChannelDispacher");
    }
}
