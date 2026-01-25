package com.ozguryazilim.telve.channel.sms;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel üzerinden gelen SMS mesajlarını dağıtır.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class SmsChannelDispacher {
   private static final Logger LOG = LoggerFactory.getLogger(SmsChannelDispacher.class);
    
    @Inject
    private SmsSender smsSender;
    
    public void execute( Exchange exchange ){
        LOG.debug("SMSMessage Dispach : {}", exchange.getIn().toString() );

       try {
           smsSender.send(exchange.getIn().getHeader("target", String.class), exchange.getIn().getBody(String.class));
           
           /*
           try {
           
           
           Map<String,DataHandler> attachments = exchange.getIn().getAttachments();
           
           if( attachments.isEmpty()){
           emailSender.send(exchange.getIn().getHeader("target", String.class),
           exchange.getIn().getHeader("subject", String.class),
           exchange.getIn().getBody(String.class));
           } else {
           emailSender.send(exchange.getIn().getHeader("target", String.class),
           exchange.getIn().getHeader("subject", String.class),
           exchange.getIn().getBody(String.class),
           attachments);
           }
           } catch (MessagingException ex) {
           LOG.error("SMS cannot send", ex);
           }
           */
       } catch (MessagingException ex) {
           LOG.error("SMS cannot send", ex);
       }
    } 
}
