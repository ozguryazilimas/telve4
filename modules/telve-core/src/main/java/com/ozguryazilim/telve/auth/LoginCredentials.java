package com.ozguryazilim.telve.auth;

import javax.enterprise.inject.Model;

/**
 *
 * @author oyas
 */
@Model
public class LoginCredentials {
    
    private String username;
    private String password;
    private Boolean rememberMe = Boolean.FALSE;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    
}
