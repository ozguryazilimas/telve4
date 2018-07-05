/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.passwordDefinitions;

import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.telve.auth.UserService;
import com.ozguryazilim.telve.config.AbstractOptionPane;
import com.ozguryazilim.telve.config.OptionPane;
import com.ozguryazilim.telve.config.OptionPaneType;
import com.ozguryazilim.telve.idm.config.IdmPages;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author yusuf
 */
@OptionPane(permission = "PasswordDefinitions", optionPage = IdmPages.PasswordDefinitions.class, type = OptionPaneType.System)
public class PasswordDefinitions extends AbstractOptionPane {

    private static final String PASSWORD_LOWERCASE = "password.lowercase";//kaç adet küçük harf olacak.
    private static final String PASSWORD_UPPERCASE = "password.uppercase";//kaç adet büyük harf olacak.
    private static final String PASSWORD_LENGTH = "password.length";//minimum uzunluk ve üstü için
    private static final String PASSWORD_SPECIAL = "password.special";//Noktalama işaretleri kaç adet olacak.
    private static final String PASSWORD_NUMBER = "password.number";//Kaç adet sayı içerecek.

    @Inject
    private Kahve kahve;

    private KahveEntry lowerCase;//kahve db içinde tutulcak veri modeli
    private KahveEntry upperCase;
    private KahveEntry length;
    private KahveEntry special;
    private KahveEntry number;

    @PostConstruct
    public void init() {
        lowerCase = kahve.get(PASSWORD_LOWERCASE, "0");
        upperCase = kahve.get(PASSWORD_UPPERCASE, "0");
        length = kahve.get(PASSWORD_LENGTH, "8");
        special = kahve.get(PASSWORD_SPECIAL, "0");
        number = kahve.get(PASSWORD_NUMBER, "8");

    }

    @Override
    public void save() {
        kahve.put(PASSWORD_LOWERCASE, lowerCase);
        kahve.put(PASSWORD_NUMBER, number);
        kahve.put(PASSWORD_UPPERCASE, upperCase);
        kahve.put(PASSWORD_SPECIAL, special);
        kahve.put(PASSWORD_LENGTH, length);
    }

    public void handleKeyEvent() {
        length.setAsString(Integer.toString(lowerCase.getAsInteger() + upperCase.getAsInteger() + special.getAsInteger() + number.getAsInteger()));
    }

    public KahveEntry getLowerCase() {
        return lowerCase;
    }

    public KahveEntry getUpperCase() {
        return upperCase;
    }

    public KahveEntry getLength() {
        return length;
    }

    public KahveEntry getSpecial() {
        return special;
    }

    public KahveEntry getNumber() {
        return number;
    }

    public void setLowerCase(KahveEntry lowerCase) {
        this.lowerCase = lowerCase;
    }

    public void setUpperCase(KahveEntry upperCase) {
        this.upperCase = upperCase;
    }

    public void setLength(KahveEntry length) {
        this.length = length;
    }

    public void setSpecial(KahveEntry special) {
        this.special = special;
    }

    public void setNumber(KahveEntry number) {
        this.number = number;
    }

}
