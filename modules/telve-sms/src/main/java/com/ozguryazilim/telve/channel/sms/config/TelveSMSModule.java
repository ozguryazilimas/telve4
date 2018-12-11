/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel.sms.config;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.channel.ChannelRegistery;
import javax.annotation.PostConstruct;

/**
 *
 * @author oyas
 */
@TelveModule
public class TelveSMSModule {
    @PostConstruct
    public void init(){
        ChannelRegistery.register("smsChannel", "sms");
    }
}
