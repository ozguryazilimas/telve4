package com.ozguryazilim.telve.reminder;

import com.google.common.base.Splitter;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.telve.channel.ChannelRegistery;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Reminder üretmek ve yönetmek için API.
 * 
 * Sistem içerisinde kullaılacak olan hatırlatma işlemler için bu sınıf üzerinden gerekli methodlar çağrılarak yapılabilir.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class ReminderService {
 
    @Inject
    private CommandSender commandSender;
    
    @Inject
    private Kahve kahve;
    
    public void createReminder( ReminderCommand reminderCommand ){

        //FIXME: Bu servis çalışıyor mu ki hala?
        ScheduledCommand sc = new ScheduledCommand( new Date().toString(), reminderCommand.getSchedule(), "SYSTEM", reminderCommand);
        
        commandSender.sendCommand(sc);
    }
    
    
    public void createReminder( String subject, String target, String schedule  ){

        ReminderCommand c = new ReminderCommand();
        c.setSchedule(schedule);
        c.setTarget(target);
        c.setSubject(subject);
        
        createReminder(c);
    }
    
    
    /**
     * Reminder servisleri için kullanılabilecek kanal listesini döndürür.
     * @return 
     */
    public List<String> getReminderChannelList( String reminderClass ){
        
        KahveEntry kv = kahve.get("reminder.channels." + reminderClass);
        
        if( kv != null ){
            return Splitter.on(',').omitEmptyStrings().trimResults().splitToList( kv.getAsString() );
        } else {
            //FIXME: GENERIC kanallara bakalım mı? yoksa hiç mi dolaşıma sokmayalım?
            return ChannelRegistery.getChannels();
        }
        
    }
    
}
