/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.passwordDefinitions;

import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author yusuf
 */
@Named
@RequestScoped
public class PasswordDefinitionsControl implements Serializable {

    private boolean lowerCase;
    private boolean upperCase;
    private boolean number;
    private int length;
    private boolean special;
    private boolean definitionsControl = false;

    @Inject
    private Kahve kahve;

    @PostConstruct
    public void init() {
        lowerCase = kahve.get("password.lowercase", true).getAsBoolean();
        upperCase = kahve.get("password.uppercase", true).getAsBoolean();
        length = kahve.get("password.length", "8").getAsInteger();
        special = kahve.get("password.special", false).getAsBoolean();
        number = kahve.get("password.number", true).getAsBoolean();
    }

    public boolean passwordControl(String password) {

        if (password.equals(password.toLowerCase()) && upperCase == true) {
            FacesMessages.error("passwordDefinitions.error.upperCase");
            definitionsControl = true;
        }
        if (password.equals(password.toUpperCase()) && lowerCase == true) {
            FacesMessages.error("passwordDefinitions.error.lowerCase");
            definitionsControl = true;
        }
        if (password.matches("[A-Za-z0-9 ]*") && special == true) {
            FacesMessages.error("passwordDefinitions.error.special");
            definitionsControl = true;
        }
        if (password.length() < length) {
            FacesMessages.error(Messages.getMessageFromData(Messages.getCurrentLocale(),"passwordDefinitions.error.length$%&"+length));
            definitionsControl = true;
        }
        if (number == true) {
            boolean hasNumber = false;
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) {
                    hasNumber = true;
                    break;
                }
            }
            if (!hasNumber) {
                FacesMessages.error("passwordDefinitions.error.number");
                definitionsControl = true;
            }
        }
        return definitionsControl;

    }

    public boolean isLowerCase() {
        return lowerCase;
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public boolean isNumber() {
        return number;
    }

    public int getLength() {
        return length;
    }

    public boolean getSpecial() {
        return special;
    }

}
