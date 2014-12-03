/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.messagebus.command.CommandScheduler;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;

/**
 * Zamanlanmış Görevler için tarama UI control sınıfı.
 * 
 * Standart browse sınıfı değildir.
 * 
 * @author Hakan Uygun
 */
@WindowScoped
@Named
public class ScheduledCommandBrowse implements Serializable{
    
    @Inject
    private CommandScheduler scheduler;
    
    public List<TimerPack> getTimers(){
        List<TimerPack> ls = new ArrayList<>();
        for( Timer t : scheduler.getTimers()){
            ls.add( new TimerPack( t));
        }
        return ls;
    }
    
    public class TimerPack implements Serializable{
        
        private long timeRemaining;
        private Date nextTimeout;
        private ScheduleExpression schedule;
        private ScheduledCommand scheduledCommand;

        public TimerPack( Timer timer) {
            this.nextTimeout = timer.getNextTimeout();
            this.schedule = timer.getSchedule();
            this.timeRemaining = timer.getTimeRemaining();
            this.scheduledCommand = (ScheduledCommand) timer.getInfo();
        }

        public TimerPack(long timeRemaining, Date nextTimeout, ScheduleExpression schedule, ScheduledCommand scheduledCommand) {
            this.timeRemaining = timeRemaining;
            this.nextTimeout = nextTimeout;
            this.schedule = schedule;
            this.scheduledCommand = scheduledCommand;
        }

        public long getTimeRemaining() {
            return timeRemaining;
        }

        public void setTimeRemaining(long timeRemaining) {
            this.timeRemaining = timeRemaining;
        }

        public Date getNextTimeout() {
            return nextTimeout;
        }

        public void setNextTimeout(Date nextTimeout) {
            this.nextTimeout = nextTimeout;
        }

        public ScheduleExpression getSchedule() {
            return schedule;
        }

        public void setSchedule(ScheduleExpression schedule) {
            this.schedule = schedule;
        }

        public ScheduledCommand getScheduledCommand() {
            return scheduledCommand;
        }

        public void setScheduledCommand(ScheduledCommand scheduledCommand) {
            this.scheduledCommand = scheduledCommand;
        }

    }
}
