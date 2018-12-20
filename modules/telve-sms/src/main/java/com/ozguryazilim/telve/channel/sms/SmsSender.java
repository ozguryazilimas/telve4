package com.ozguryazilim.telve.channel.sms;

import com.ozguryazilim.telve.audit.AuditLogger;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hakan Uygun
 */
@Dependent
@Named
public class SmsSender implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsSender.class);

    @Inject
    private SmsService smsService;
    
    @Inject
    private AuditLogger auditLogger;
    
    public void send(String to, String body) throws MessagingException {
        smsService.send(to, body);
        auditLogger.actionLog("NOTIFICATION", 0l, to, "SMS", "SEND", "SYSTEM", body);
    }
    
    
}
