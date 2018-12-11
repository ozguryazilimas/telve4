/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.messagebus.command.CommandScheduler;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import com.ozguryazilim.telve.utils.ScheduleModel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 * Rapor Zamanlama dialoğu
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class ReportScheduleDialog implements Serializable{
    
    private ReportCommand command;
    
    
    private String scheduleType;
    private Date startDate;
    private Date endDate;
    private String schedule;
    
    @Inject
    private CommandScheduler scheduler;
    
    @Inject
    private Identity userIdentity;
    
    public void openDialog( ReportCommand command ){
        this.command = command;
        scheduleType = "O";
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        RequestContext.getCurrentInstance().openDialog("/reports/scheduleReportDialog", options, null);
    }

    public void schedule(){
     
        String s = buildScheduleExpression();
        //Eğer tanımlı bir schedule yoksa çık
        if( Strings.isNullOrEmpty(s)) return;
        
        //Eğer komut hali hazırda zamanlanmış durumda ise önce onu siler.
        //if( selectedItem.getScheduledCommand() != null ){
        //    scheduler.removeFromScedular(selectedItem.getScheduledCommand());
        //}
        
        //Şimdi komutu scheduler'a ekliyoruz.
        //TODO: Kullanıcı bilgisini alıp eklemek lazım.
        ScheduledCommand sc = new ScheduledCommand(UUID.randomUUID().toString(), s, userIdentity.getUserInfo().getId(), command);
        scheduler.addToSceduler(sc);
        
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    
     /**
     * UI'dan gelen verileri kullanarak SC için schdule oluşturur.
     * @return 
     */
    protected String buildScheduleExpression(){
        
        switch ( scheduleType ){
            case "O" : return ScheduleModel.getOnceExpression(startDate);
            case "P" : return ScheduleModel.getPeriodExpression(schedule, startDate);
            case "S" : return ScheduleModel.getShortScheduleExpression(schedule, startDate, endDate);
            case "SE" : return ScheduleModel.getScheduledExpression(schedule, startDate, endDate);
        }
        
        return "";
    }
    
    
    public void cancelDialog(){
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    public ReportCommand getCommand() {
        return command;
    }

    public void setCommand(ReportCommand command) {
        this.command = command;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    
    

    
    
    
}
