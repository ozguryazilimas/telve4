package com.ozguryazilim.telve.auth;

/**
 * Bir kullanıcı LoggedIn olduğunda fırlatılır.
 * @author Hakan Uygun
 */
public class LoggedInEvent {

    private boolean changePassword;

    //Gelen değer true ise giriş yapan kullanıcı şifre değiştirmeli
    public LoggedInEvent(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }
}
