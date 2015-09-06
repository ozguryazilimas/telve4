/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.todo;

import com.ozguryazilim.telve.bpm.handlers.AbstractHumanTaskHandler;
import com.ozguryazilim.telve.bpm.handlers.HumanTaskHandler;

/**
 * Standart TodoTask için handler
 * @author Hakan Uygun
 */
@HumanTaskHandler(taskName = "todoTask", icon = "fa-check-square-o")
public class TodoTask extends AbstractHumanTaskHandler{
    
    @Override
    public String getDialogName() {
        return "/bpm/todoTaskPopup";
    }

    @Override
    public String getViewId() {
        return "/bpm/todoTaskPopup.xhtml";
    }
    
}
