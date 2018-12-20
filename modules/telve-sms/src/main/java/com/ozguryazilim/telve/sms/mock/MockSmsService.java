package com.ozguryazilim.telve.sms.mock;

import com.ozguryazilim.telve.channel.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Development ve Test sırasında kullanılabilcek Mock SMS servisi
 * @author Hakan Uygun
 */
public class MockSmsService implements SmsService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MockSmsService.class);
    
    @Override
    public void send(String to, String body) {
        LOGGER.warn("MOCK SMS Service Executed!");
        LOGGER.info("SMS: {} {}", to, body);
    }
    
}
