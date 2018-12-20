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
