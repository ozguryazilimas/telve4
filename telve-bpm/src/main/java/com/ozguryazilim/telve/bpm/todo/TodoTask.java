/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.todo;

import java.util.Date;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

import com.ozguryazilim.telve.bpm.TaskPages;
import com.ozguryazilim.telve.bpm.handlers.AbstractHumanTaskHandler;
import com.ozguryazilim.telve.bpm.handlers.HumanTaskHandler;

/**
 * Standart TodoTask için handler
 * @author Hakan Uygun
 */
@HumanTaskHandler(taskName = "todoTask", icon = "fa-check-square-o")
public class TodoTask extends AbstractHumanTaskHandler{
    
    private String subject;
    private String info;
    private Date startDate;
    private Date endDate;
    
    @Override
    public String getDialogName() {
        return "/bpm/todoTaskPopup";
    }

    @Override
    public String getViewId() {
        return "/bpm/todoTaskPopup.xhtml";
    }

    @Override
    protected void popVariables() {
	subject = (String) getTask().getVariables().get("SUBJECT");
	info = (String) getTask().getVariables().get("INFO");
	startDate = (Date)getTask().getVariables().get("START_DATE");
	endDate = (Date)getTask().getVariables().get("END_DATE");
    }
    
	@Override
	public Class<? extends ViewConfig> getDialogViewConfig() {
		return TaskPages.TodoTaskPopup.class;
	}
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
}
