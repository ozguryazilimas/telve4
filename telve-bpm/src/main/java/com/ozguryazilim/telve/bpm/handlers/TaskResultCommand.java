/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import java.io.Serializable;

/**
 * Task UI'da task ile ilgili alınabilecek aksionlar için Model sınıf
 * @author Hakan Uygun
 */
public class TaskResultCommand implements Serializable{

    
    public static final TaskResultCommand ACCEPT = new TaskResultCommand("ACCEPT", "fa fa-check", "btn-success");
    public static final TaskResultCommand REJECT = new TaskResultCommand("REJECT", "fa fa-close", "btn-danger");
    public static final TaskResultCommand COMPLETE = new TaskResultCommand("COMPLETE", "fa fa-check", "btn-success");
    
    private String result;
    private String icon;
    private String style;

    public TaskResultCommand(String result, String icon, String style) {
        this.result = result;
        this.icon = icon;
        this.style = style;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    
    
}
