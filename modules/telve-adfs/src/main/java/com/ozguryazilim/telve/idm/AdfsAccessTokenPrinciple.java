package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.TelveIdmPrinciple;
import com.ozguryazilim.telve.idm.helpers.IdentityContextData;
import java.io.Serializable;

/**
 *
 * @author oyas
 */
public class AdfsAccessTokenPrinciple implements TelveIdmPrinciple, Serializable{

    private IdentityContextData context;

    public AdfsAccessTokenPrinciple(IdentityContextData context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return context.getAccount().username().split("@")[0];
    }

    public IdentityContextData getContext() {
        return context;
    }

    
    
}
