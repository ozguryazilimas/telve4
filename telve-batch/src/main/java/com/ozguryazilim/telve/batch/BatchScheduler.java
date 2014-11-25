/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.entities.JobDefinition;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * Sistemde tanımlı görevleri tarayarak zamanlanmasını sağlar.
 * 
 * @author Hakan Uygun
 */
@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class BatchScheduler {
   
    @Resource
    TimerService timerService;
    
    @Inject
    private JobDefinitionRepository repository;
    
    @Inject
    private BatchStarter batchStarter;
    
    @PostConstruct
    public void initTimer() {
        
        List<JobDefinition> jobs = repository.findAllActives();
        
        //Önce daha önceden kayıtlı varsa onları bir siliyoruz.
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if ( jobs.contains(timer.getInfo()) ) {
                    timer.cancel();
                }
            }
        }
        
        for( JobDefinition jd : jobs ){
            //TODO: Burası jobDefinitiondan alınacak
            timerService.createCalendarTimer(
                    new ScheduleExpression().
                    hour("*").
                    minute("*").
                    second("*/10"), new TimerConfig( jd, true));
        }
        
        timerService.createCalendarTimer(
                    new ScheduleExpression().
                    hour("*").
                    minute("*").
                    second("*/10"), new TimerConfig( "HearthBeat", true));
        
    }

    public void addToScedular( JobDefinition jd ){
        System.out.println( "Job Scheduling : " + jd );
        timerService.createCalendarTimer(
                    new ScheduleExpression().
                    hour("*").
                    minute("*").
                    second("*/10"), new TimerConfig( jd, true));
    }
    
    public void removeFromScedular( JobDefinition jd ){
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if ( timer.getInfo().equals(jd) ) {
                    timer.cancel();
                }
            }
        }
    }
    
    @Timeout
    public void timeout(Timer timer) {
        System.out.println(getClass().getName() + ": " + new Date() + " " + timer.getInfo());
        if( timer.getInfo() instanceof JobDefinition ){
            JobDefinition jd = (JobDefinition) timer.getInfo();
            batchStarter.start(jd);
        }
    }
}
