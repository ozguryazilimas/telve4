/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notification;

import com.google.common.base.Splitter;
import com.ozguryazilim.telve.channel.ChannelRegistery;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Notification gönderileri için temel servis sınıfı.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class NotificationService {
    
    
    @Inject
    private CommandSender commandSender;
    
    
    /**
     * Geriye verilen notfication sınıfı için kullanılabilir channel listesini döndürür.
     * 
     * @param notificationClass
     * @return 
     */
    public List<String> getNotificationChannelList( String notificationClass ){
        
        //FIXME: UI yapamalı mı?
        String ls = ConfigResolver.getPropertyAwarePropertyValue("notification.channels",  notificationClass, "email,web");
        
        List<String> ts = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(ls);
        List<String> results = new ArrayList<>();
        
        for( String a : ts ){
            results.add( ChannelRegistery.getChannelByAlias(a));
        }
        
        //TODO: Eğer bu bahsi geçen kanallar register edilmemiş ise ne olacak?
        return results;
    }
}
