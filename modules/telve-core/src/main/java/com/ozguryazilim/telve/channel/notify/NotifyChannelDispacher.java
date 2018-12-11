/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.notify;

import com.ozguryazilim.telve.notify.NotifyHandler;
import com.ozguryazilim.telve.notify.NotifyMessage;
import com.ozguryazilim.telve.notify.NotifyStore;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel üzerinden gelen Notify mesajlarını dağıtır.
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class NotifyChannelDispacher {
    private static final Logger LOG = LoggerFactory.getLogger(NotifyChannelDispacher.class);

    @Inject
    NotifyStore notifyStore;
    
    @Inject
    private NotifyHandler notifyHandler;
    
    public void execute( Exchange exchange ){
        LOG.warn("NotifyMessage Dispach : {}", exchange.getIn().toString() );
    
        NotifyMessage message = new NotifyMessage(
                exchange.getIn().getHeader("target", String.class),
                exchange.getIn().getHeader("subject", String.class),
                exchange.getIn().getBody(String.class), 
                exchange.getIn().getHeader("link", String.class),
                exchange.getIn().getHeader("icon", String.class),
                exchange.getIn().getHeader("severity", String.class)
        );
        
        notifyStore.save(message);
        notifyHandler.sendMessage(message);
    }
}
