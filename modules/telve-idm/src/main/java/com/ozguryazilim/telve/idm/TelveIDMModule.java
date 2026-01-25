package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.auth.UserModelRegistery;
import com.ozguryazilim.telve.suggestion.SuggestionGroupRegistery;
import jakarta.annotation.PostConstruct;

/**
 * Telve Admin Module Tanım Sınıfı.
 * @author Hakan Uygun
 */
@TelveModule
public class TelveIDMModule {

    @PostConstruct
    public void init(){
        SuggestionGroupRegistery.intance().addGroup("userGroup", Boolean.FALSE);
        UserModelRegistery.registerUserType("STANDART");
        UserModelRegistery.registerUserType("SUPERADMIN");
    }
}
