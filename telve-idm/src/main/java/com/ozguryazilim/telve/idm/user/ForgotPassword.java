package com.ozguryazilim.telve.idm.user;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class ForgotPassword {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
