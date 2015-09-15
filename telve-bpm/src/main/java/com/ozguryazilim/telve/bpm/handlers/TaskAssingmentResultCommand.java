/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import com.ozguryazilim.telve.auth.UserInfo;
import com.ozguryazilim.telve.bpm.TaskInfo;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Assignee değeri dolduran TaskResultCommand.
 * 
 * Execute sırasında sadece RESULT değerini değil ASSINGNEE değerini de doldurur.
 * 
 * Değere aktif Identity'nin id'sini yerleştirir.
 * 
 * @author Hakan Uygun
 */
public class TaskAssingmentResultCommand extends TaskResultCommand{

    public TaskAssingmentResultCommand(String result, String icon, String style) {
        super(result, icon, style);
    }

    @Override
    public void execute(TaskInfo task) {
        super.execute(task); 
        
        //Mevcut identity'i bulalım
        UserInfo id = BeanProvider.getContextualReference(UserInfo.class, true);
        //Şimdide bunun atamasını yapalım.
        task.getVariables().put("ASSIGNEE", id.getLoginName());
    }
    
}
