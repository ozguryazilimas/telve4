package com.ozguryazilim.telve.messagebus.command;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sadece Command interface'i ile komut gelirse onları dinleyip log'a yazar.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = Command.class)
public class DefaultCommandExecutor {
    
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCommandExecutor.class);
    
    public void execute( Exchange exchange ){
        LOG.warn("Executor not registered for Command : {}", exchange.getIn().toString() );
    }
}
