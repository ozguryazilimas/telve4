/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.sms.config;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.channel.sms.SmsService;
import com.ozguryazilim.telve.sms.mock.MockSmsService;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.LoggerFactory;

/**
 * Config değerine bakarak hangi SMS Servis sağlayıcıyı kullanacağına karar verip onun servisini döndürür.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class SmsSrviceProducer implements Serializable{
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SmsSrviceProducer.class);
    
    @Produces
    public SmsService getSmsService() {
        String serviceName = ConfigResolver.getPropertyValue("sms.serviceName");
        if( !Strings.isNullOrEmpty(serviceName)){
            try {
                return (SmsService) this.getClass().getClassLoader().loadClass(serviceName).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                LOGGER.error("SMS Service cannot Loaded", ex);
                return new MockSmsService();
            }
        } else {
            return new MockSmsService();
        }
    }
}
