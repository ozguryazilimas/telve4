/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reminder;

import com.ozguryazilim.telve.channel.Channel;
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
    
    @Override
    public void execute(ReminderCommand command) {
        //FIXME: Burada gelen komutun payload'una bakarak gerekli hatırlatma işlemleri yapılacak.
        LOG.info("ReminderCommandExecutor execute : {}", command);
        
        //Önce target çözümlemesi yapılmalı. Hatırlatma kimlere yapılacak?
        //Şimdilik sadece bir kişi kabul ediyoruz. Target üzerinde kullanıcının bilgisi var.
        List<String> targets = new ArrayList<>();
        targets.add(command.getTarget());
        
        //Hatırlatma türüne bakarak hangi kanalları kullanabileceğimize bakacağız ve bunların detay bilgilerini öğreneceğiz
        List<String> channels = reminderService.getReminderChannelList(command.getReminderClass());
        
        
        
        //Şimdi her kanal'a ilgili kişi ile birlikte ihtiyaç duyduğu detay bilgileri gönderiyoruz.
        for( String t : targets ){
            //Hatırlatma yapılacak kişinin üzerinde bulunan kurallara bakıyoruz hangi kanalları kullanabiliriz.
            //FIXME: Kişiye ait geçerli kanallar alınacak
            
            for( String channelName : channels ){
                Channel channel = (Channel) BeanProvider.getContextualReference(channelName);
                channel.sendMessage( t, command.getSubject(), "", command.getParams());
            }
        }
        
    }
    
}
