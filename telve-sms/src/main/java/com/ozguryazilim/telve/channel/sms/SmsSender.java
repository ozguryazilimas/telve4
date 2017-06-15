/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.sms;

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
    SmsService smsService;
    
    public void send(String to, String body) throws MessagingException {
        smsService.send(to, body);
    }
    
    
}
