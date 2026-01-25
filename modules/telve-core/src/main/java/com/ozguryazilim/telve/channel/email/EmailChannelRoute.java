package com.ozguryazilim.telve.channel.email;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.ContextName;

/**
 * EMailChannel için Camel Route tanımı
 * @author Hakan Uygun
 */
// FIXME: jakarta: camel 4 ile beraber camel-cdi kalkmış yerine ne kullanacağız?
@Dependent //@ContextName("telve")
public class EmailChannelRoute extends RouteBuilder{
    
    @Inject
    private EmailChannelProcessor channelProcessor;
    
    @Override
    public void configure() throws Exception {
        //Gelen mesajın hangi template'i kullanacağı processor ile bulunup header.templateName'e konuyor
        from("seda:emailChannel")
           .process(channelProcessor)
           .recipientList(simple("${header.templateName}"))
           .to("bean:emailChannelDispacher");
    }
}
