/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.sms.mock;

import com.ozguryazilim.telve.channel.sms.SmsService;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
@Dependent
@Named
public class MockSmsService implements SmsService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MockSmsService.class);
    
    @Override
    public void send(String to, String body) {
        LOGGER.info("MOCK SMS Service Executed!");
        LOGGER.info("SMS: {} {}", to, body);
    }
    
}
