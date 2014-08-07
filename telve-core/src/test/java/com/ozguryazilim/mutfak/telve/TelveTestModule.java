/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.mutfak.telve;

import com.ozguryazilim.telve.api.module.TelveModule;
import javax.annotation.PostConstruct;

/**
 *
 * @author haky
 */
@TelveModule()
public class TelveTestModule {
    
    private String testMessage;
    
    @PostConstruct
    public void init(){
        testMessage = "Inited";
    }

    public String getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }
    
    
}
