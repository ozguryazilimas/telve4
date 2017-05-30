/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports.schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.messagebus.command.CommandScheduler;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import com.ozguryazilim.telve.utils.ScheduleModel;
import com.ozguryazilim.telve.view.DialogBase;
import com.ozguryazilim.telve.view.Pages;

/**
 * Rapor Zamanlama dialoğu
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class ReportScheduleDialog extends DialogBase implements Serializable{
    
    private ReportCommand command;
    
    
    private String scheduleType;
    private Date startDate;
    private Date endDate;
    private String schedule;
    
    @Inject
    private CommandScheduler scheduler;
    
	public void openDialog( ReportCommand command ) {
		this.command=command;
		scheduleType = "O";
		openDialog();
	}

	@Override
	public void closeDialog() {
		schedule();
		closeDialogWindow();
	}

	@Override
	public Class<? extends ViewConfig> getDialogViewConfig() {
		return Pages.Reports.ScheduleReportDialog.class;
	}

	@Override
	protected void decorateDialog(Map<String, Object> options) {
		options.put("modal", true);
		options.put("resizable", false);
		options.put("contentHeight", 450);
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
        ScheduledCommand sc = new ScheduledCommand(UUID.randomUUID().toString(), s, command.getUser(), command);
        scheduler.addToSceduler(sc);
        
        closeDialogWindow();
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
