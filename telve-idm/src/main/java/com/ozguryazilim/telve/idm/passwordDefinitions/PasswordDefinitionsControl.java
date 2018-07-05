/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.passwordDefinitions;

import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.Serializable;
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

    private int lowerCase;
    private int upperCase;
    private int number;
    private int length;
    private int special;
    
    private int lowerCaseCount = 0;
    private int upperCaseCount = 0;
    private int numberCount = 0;
    private int specialCount = 0;
    private boolean definitionsControl = false;

    @Inject
    private Kahve kahve;
    
    @PostConstruct
    public void init()
    {
        lowerCase = kahve.get("password.lowercase").getAsInteger();
        upperCase = kahve.get("password.uppercase").getAsInteger();
        length = kahve.get("password.length").getAsInteger();
        special = kahve.get("password.special").getAsInteger();
        number = kahve.get("password.number").getAsInteger();
    }
    
    public boolean passwordControl(String password) {
       
        String temp = "";
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                lowerCaseCount++;
            } else if (Character.isUpperCase(password.charAt(i))) {
                upperCaseCount++;
            } else if (Character.isDigit(password.charAt(i))) {
                numberCount++;
            } else {
                specialCount++;
            }
        }
        if (lowerCaseCount < lowerCase) {
            FacesMessages.error(Integer.toString(lowerCase-lowerCaseCount),"passwordDefinitions.error.lowerCase");
            definitionsControl = true;
        }  
        if (upperCaseCount < upperCase) {
            FacesMessages.error(Integer.toString(upperCase-upperCaseCount),"passwordDefinitions.error.upperCase");
            definitionsControl = true;
        }  
        if (numberCount < number) {
            FacesMessages.error(Integer.toString(number-numberCount),"passwordDefinitions.error.number");
            definitionsControl = true;
        } 
        if (specialCount < special) {
            FacesMessages.error(Integer.toString(special-specialCount),"passwordDefinitions.error.special");
            definitionsControl = true;
        }

        return definitionsControl;

    }

    public int getLowerCase() {
        return lowerCase;
    }

    public int getUpperCase() {
        return upperCase;
    }

    public int getNumber() {
        return number;
    }

    public int getLength() {
        return length;
    }

    public int getSpecial() {
        return special;
    }
    

}
