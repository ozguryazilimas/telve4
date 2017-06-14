/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notification;

import com.ozguryazilim.telve.channel.Channel;
import com.ozguryazilim.telve.contact.Contact;
import com.ozguryazilim.telve.contact.ContactResolver;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notification Komut çalıştırıcı.
 * 
 * Gelen notification komutlarını, target çözümlemesi yaparak ilgili channellara yönlendirir.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = NotificationCommand.class)
public class NotificationCommandExecutor extends AbstractCommandExecuter<NotificationCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(NotificationCommandExecutor.class);
    
    @Inject
    private ContactResolver contactResolver;
    
    @Inject
    private NotificationService notificationService;
    
    @Override
    public void execute(NotificationCommand command) {
    
        LOG.info("NotificationCommandExecutor execute : {}", command);
        
        //Önce target çözümlemesi yapılmalı. Hatırlatma kimlere yapılacak?
        List<Contact> targets = new ArrayList<>();
        targets.addAll(contactResolver.resolveContacts(command.getTarget()));
        
        //Gönderi türüne bakarak hangi kanalları kullanabileceğimize bakacağız ve bunların detay bilgilerini öğreneceğiz
        List<String> channels = notificationService.getNotificationChannelList(command.getNotificationClass());
        
        LOG.debug("Notification send for channels : {}", channels);
        
        
        //Şimdi her kanal'a ilgili kişi ile birlikte ihtiyaç duyduğu detay bilgileri gönderiyoruz.
        for( Contact t : targets ){
            //Hatırlatma yapılacak kişinin üzerinde bulunan kurallara bakıyoruz hangi kanalları kullanabiliriz.
            //FIXME: Kişiye ait geçerli kanallar alınacak. Bu channell + messageClass + target şeklinde olmalı
            
            for( String channelName : channels ){
                Channel channel = (Channel) BeanProvider.getContextualReference(channelName);
                
                if( channel.isValidContact(t) ){
                    t.pushToMap(command.getParams());
                    command.getParams().put("messageClass", command.getNotificationClass());
                    command.getParams().put("template", command.getTemplate());
                    command.getParams().put("sender", command.getSender());
                    channel.sendMessage( t, command.getSubject(), "", command.getParams());
                } else {
                    LOG.debug("Channel {} not support for this contact {}", channelName, t);
                }
            }
        }
    }
    
}
