/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.admin;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.suggestion.SuggestionGroupRegistery;
import javax.annotation.PostConstruct;

/**
 * Telve Admin Module Tanım Sınıfı.
 * @author Hakan Uygun
 */
@TelveModule
public class TelveIDMModule {

    @PostConstruct
    public void init(){
        SuggestionGroupRegistery.intance().addGroup("userGroup", Boolean.FALSE);
    }
}
