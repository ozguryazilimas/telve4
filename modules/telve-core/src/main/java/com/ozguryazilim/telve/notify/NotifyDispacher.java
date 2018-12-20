package com.ozguryazilim.telve.notify;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class NotifyDispacher {
    
    @Inject
    private NotifyHandler notifyHandler;
    
    public void execute( NotifyMessage message ){
        notifyHandler.sendMessage(message);
    }
}
