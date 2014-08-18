/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.config;

import com.ozguryazilim.telve.utils.CookieUtils;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

/**
 * Kişiye özel olarak tema desteği sağlar.
 *
 * 
 * Cookie'ye bakar, yoksa kullanıcı için tanımlı thema bakar,yoksa default değer atar.
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class ThemeSelector implements Serializable {

    private static final String THEME_COOKIE = "telve.theme";

    private String theme;

    @Inject
    private TelveConfigResolver configResolver;

    public String getTheme() {
        if (theme == null) {

            //Önce Cookie var mı diye bakalım.
            Cookie c = CookieUtils.getCookie(THEME_COOKIE);
            if (c != null) {
                theme = c.getValue();
            } else {

                String t = configResolver.getProperty("theme.name");
                //Config'de bir değer yoksa default bootstrap yapalım...
                setTheme( t == null ? "bootstrap" : t );
            }
        }
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;

        CookieUtils.setCookie("telve.theme", theme, -1);

    }

}
