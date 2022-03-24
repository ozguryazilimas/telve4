package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.TelveIdmPrinciple;
import com.ozguryazilim.telve.idm.helpers.IdentityContextData;
import java.io.Serializable;

/**
 *
 * @author oyas
 */
public class AdsfAccessTokenPrinciple implements TelveIdmPrinciple, Serializable{

    private IdentityContextData context;

    public AdsfAccessTokenPrinciple(IdentityContextData context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return context.getUsername();
    }

    public IdentityContextData getContext() {
        return context;
    }

    
    
}
