/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notification;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.channel.ChannelRegistery;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notification gönderileri için temel servis sınıfı.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class NotificationService {
    
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class); 
    
    @Inject
    private CommandSender commandSender;
    
    
    /**
     * Geriye verilen notfication sınıfı için kullanılabilir channel listesini döndürür.
     * 
     * @param notificationClass
     * @param template
     * @return 
     */
    public List<String> getNotificationChannelList( String notificationClass, String template ){
        
        //TODO: UI yapamalı mı?
        
        //Once sınıf + template için bak
        String ls = ConfigResolver.getPropertyValue("notification.channels." +  notificationClass + "." + template);
        if( Strings.isNullOrEmpty(ls)){
            //Bulamazsan sadece sınıf için bak
            ls = ConfigResolver.getPropertyValue("notification.channels." +  notificationClass );
        }
        
        if( Strings.isNullOrEmpty(ls)){
            //Bulamazsan genel olan değeri al
            ls = ConfigResolver.getPropertyValue("notification.channels", "email,web");
        }
        
        List<String> ts = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(ls);
        List<String> results = new ArrayList<>();
        
        ts.forEach((a) -> {
            String s = ChannelRegistery.getChannelByAlias(a);
            if( !Strings.isNullOrEmpty(s)){
                results.add( s );
            } else {
                LOG.warn("Channel {} not registered", a );
            }
        });
        
        return results;
    }
}
