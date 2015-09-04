/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import com.ozguryazilim.telve.utils.ScheduleModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zamanlanmış komutlar için TimerService arayüzü.
 *
 * @author Hakan Uygun
 */
@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class CommandScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(CommandScheduler.class);

    @Resource
    TimerService timerService;

    @Inject
    private CommandSender commandSender;
    
    @PostConstruct
    public void initTimer() {

        LOG.info("Active scheduled commands :");
        //Önce daha önceden kayıtlı varsa onları bir siliyoruz.
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if (timer.getInfo() instanceof ScheduledCommand) {
                    ScheduledCommand command = (ScheduledCommand) timer.getInfo();
                    LOG.info("Command {} scheduled as {} by {}", new Object[]{command.getCommand(), command.getSchedule(), command.getCreateBy()});
                } else {
                    //Demek ki yanlış bişiler var uçuralım...
                    timer.cancel();
                }
            }
        }
    }

    public List<Timer> getTimers() {
        return new ArrayList(timerService.getTimers());
    }

    public void addToSceduler(ScheduledCommand command) {
        
        ScheduleModel sm = ScheduleModel.fromString(command.getSchedule());
        
        switch ( sm.getType() ){
            case ScheduleExpression : 
                timerService.createCalendarTimer( sm.getExpression(), new TimerConfig(command, true));
                break;
            case ShortSchedule :
                //FIXME: bu da aslında normal bir Schedule Expresion olacak.
                timerService.createCalendarTimer( sm.getExpression(), new TimerConfig(command, true));
                break;
            case Period :
                //Period'u milisecond cinsine çeviriyoruz.
                timerService.createIntervalTimer(sm.getStartDate(), sm.getPeriod().toStandardSeconds().getSeconds() * 1000, new TimerConfig(command, true));
                break;
            case Once :
                //Bir kere verilen tarih saat içinde çalıştıracak
                timerService.createSingleActionTimer(sm.getStartDate(), new TimerConfig(command, true));
                break;
        }
    }

    public void removeFromScedular(ScheduledCommand command) {
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if (timer.getInfo().equals(command)) {
                    timer.cancel();
                }
            }
        }
    }
    
    /**
     * Verilen ID'ye sahip ScheduledCommand'ı timer'dan kaldırır.
     * @param id 
     */
    public void removeFromScedularBuID(String id) {
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if (((ScheduledCommand)timer.getInfo()).getId().equals(id)) {
                    timer.cancel();
                }
            }
        }
    }

    @Timeout
    public void timeout(Timer timer) {
        System.out.println(getClass().getName() + ": " + new Date() + " " + timer.getInfo());
        if (timer.getInfo() instanceof ScheduledCommand) {
            ScheduledCommand command = (ScheduledCommand) timer.getInfo();
            LOG.info("Command {} scheduled as {} by {} now executing", new Object[]{command.getCommand(), command.getSchedule(), command.getCreateBy()});
            //Aslında doğrudan çalıştırmak yerine çalışmak üzere tekrar yola çıkarıyoruz.
            commandSender.sendCommand(command.getCommand());
        } else {
            //Başka bişi karışmışsa araya kapatalım :)
            timer.cancel();
        }
    }
}
