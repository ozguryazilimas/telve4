/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.email;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel kuyruğundan gelen e-postaları dağıtır.
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class EmailChannelDispacher {
    
    private static final Logger LOG = LoggerFactory.getLogger(EmailChannelDispacher.class);
    
    @Inject
    private EmailSender emailSender;
    
    public void execute( Exchange exchange ){
        LOG.debug("MailMessage Dispach : {}", exchange.getIn().toString() );
    
        try {
            emailSender.send(exchange.getIn().getHeader("email", String.class),
                    exchange.getIn().getHeader("subject", String.class),
                    exchange.getIn().getBody(String.class));
        } catch (MessagingException ex) {
            LOG.error("EMail cannot send", ex);
        }
    }
}
