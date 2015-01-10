/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reminder;

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
 * ReminderCommand çalıştırıcı.
 * 
 * Gelen komutu payload içeriğine bakarak çalıştırır.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = ReminderCommand.class)
public class ReminderCommandExecutor extends AbstractCommandExecuter<ReminderCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(ReminderCommandExecutor.class);
    
    @Inject
    private ReminderService reminderService;
    
    @Inject
    private ContactResolver contactResolver;
    
    @Override
    public void execute(ReminderCommand command) {
        
        LOG.info("ReminderCommandExecutor execute : {}", command);
        
        //Önce target çözümlemesi yapılmalı. Hatırlatma kimlere yapılacak?
        List<Contact> targets = new ArrayList<>();
        targets.addAll(contactResolver.resolveContacts(command.getTarget()));
        
        //Hatırlatma türüne bakarak hangi kanalları kullanabileceğimize bakacağız ve bunların detay bilgilerini öğreneceğiz
        List<String> channels = reminderService.getReminderChannelList(command.getReminderClass());
        
        
        
        //Şimdi her kanal'a ilgili kişi ile birlikte ihtiyaç duyduğu detay bilgileri gönderiyoruz.
        for( Contact t : targets ){
            //Hatırlatma yapılacak kişinin üzerinde bulunan kurallara bakıyoruz hangi kanalları kullanabiliriz.
            //FIXME: Kişiye ait geçerli kanallar alınacak
            //FIXME: kanal için yeterli bilgi yoksa da atlanmalı
            
            for( String channelName : channels ){
                Channel channel = (Channel) BeanProvider.getContextualReference(channelName);
                t.pushToMap(command.getParams());
                channel.sendMessage( t.toString(), command.getSubject(), "", command.getParams());
            }
        }
        
    }
    
}
