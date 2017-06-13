/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.sms.config;

import com.ozguryazilim.telve.channel.sms.SmsService;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Config değerine bakarak hangi SMS Servis sağlayıcıyı kullanacağına karar verip onun servisini döndürür.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class SmsSrviceProducer {
    
    @Produces
    public SmsService getSmsService(){
        String serviceName = ConfigResolver.getPropertyValue("sms.serviceName", "mockSmsService");
        return (SmsService) BeanProvider.getContextualReference(serviceName);
    }
}
