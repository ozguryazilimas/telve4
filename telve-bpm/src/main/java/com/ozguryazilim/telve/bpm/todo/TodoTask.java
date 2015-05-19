/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.todo;

import com.ozguryazilim.telve.bpm.handlers.AbstractHumanTaskHandler;
import com.ozguryazilim.telve.bpm.handlers.HumanTaskHandler;
import java.util.Date;
import java.util.Map;

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

    @Override
    protected void popVariables(Map<String, Object> variables) {
        setSubject((String) variables.get("SUBJECT"));
        setInfo((String) variables.get("INFO"));
        setStartDate((Date) variables.get("START_DATE"));
        setEndDate((Date) variables.get("END_DATE"));
    }

    @Override
    protected void pushVariables(Map<String, Object> variables) {
        variables.put("SUBJECT", subject);
        variables.put("INFO", info);
        variables.put("START_DATE", startDate);
        variables.put("END_DATE", endDate);
    }

    @Override
    public String getViewId() {
        return "/bpm/todoTaskPopup.xhtml";
    }
    
    
    
}
