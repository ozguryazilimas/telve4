/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.camunda.bpm.engine.task.Task;

/**
 * Camunda Task üzerinden gelen bilgilerin toparlanıp sunulduğu wrapper model sınıf.
 * 
 * @author Hakan Uygun
 */
public class TaskInfo implements Serializable{
    
    private Task task;
    private Map<String,Object> variables;

    public TaskInfo(Task task, Map<String, Object> variables) {
        this.task = task;
        this.variables = variables;
    }

    /**
     * Geriye task bilgilerini döndürür.
     * @return 
     */
    public Task getTask() {
        return task;
    }

    /**
     * Task'ı setler
     * @param task 
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Task için tanımlı variable listesini döndürür.
     * @return 
     */
    public Map<String, Object> getVariables() {
        return variables;
    }

    /**
     * Task variable listesini setler
     * @param variables 
     */
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    /**
     * Geriye taskın bağlı SUBJECT değişkeninin değerini döndürür.
     * @return 
     */
    public String getSubject(){
        return (String)getVariables().get("SUBJECT");
    }

    /**
     * Subject'i değiştirir.
     * @param subject 
     */
    public void setSubject( String subject ){
        getVariables().put("SUBJECT", subject);
    }
    
    /**
     * Açıklama döndürür
     * @return 
     */
    public String getInfo(){
        return (String)getVariables().get("INFO");
    }
    
    /**
     * Açıklama setler
     * @param info 
     */
    public void setInfo( String info ){
        getVariables().put("INFO", info);
    }
    
    /**
     * Başlangıç tarihini döndürür.
     * @return 
     */
    public Date getStartDate(){
        return (Date)getVariables().get("START_DATE");
    }
    
    /**
     * Başlangıç tarihini setler
     * @param date 
     */
    public void setStartDate( Date date ){
        getVariables().put("START_DATE", date );
    }
    
    /**
     * Bitiş tarihini döndürür
     * @return 
     */
    public Date getEndDate(){
        return (Date)getVariables().get("END_DATE");
    }
    
    /**
     * Bitiş tarihini setler
     * @param date 
     */
    public void setEndDate( Date date ){
        getVariables().put("END_DATE", date );
    }
    
    /**
     * Geriye task FormKey'i döndürür.
     * @return 
     */
    public String getFormKey(){
        return getTask().getFormKey();
    }
    
    /**
     * Geriye task ID döndürür.
     * @return 
     */
    public String getId(){
        return getTask().getId();
    }
    
    /**
     * Geriye resultCommand setinin hazırlnaması için AcceptableResults listesi döner.
     * 
     * Bu liste virgüller ile ayrılmış durumdadır. İçi bş olur ise default davranış COMPLATE kullanılmalı...
     * 
     * @return 
     */
    public String getAcceptableResults(){
        return (String)getVariables().get("ACCEPTABLE_RESULTS");
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.task);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskInfo other = (TaskInfo) obj;
        if (!Objects.equals(this.task, other.task)) {
            return false;
        }
        return true;
    }
}
