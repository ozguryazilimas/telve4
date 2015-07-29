/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.config;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.utils.CookieUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Kullanıcı ayarlı dil seçimi sağlar.
 *
 * Seam 2.3'den esinlenildi.
 *
 * Default locale'i jsf-config yerine "application.locale" property değerinden okuyor. eğer bulamazsa varsayılan olarak tr döndürüyor.
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class LocaleSelector implements Serializable {

    private static final String LOCALE_COOKIE = "telve.locale";

    private Locale locale;
    private String language;
    private String country;
    private String variant;

    @PostConstruct
    public void init() {
        //Önce Cookie'ye bir bakalım 
        Cookie c = CookieUtils.getCookie(LOCALE_COOKIE);
        if (c != null) {
            setLocaleString(c.getValue());
        } else {
            //Yoksa config değerini alalım.
            setLocaleString(ConfigResolver.getPropertyValue("application.locale", "tr"));
        }
    }

    public Locale calculateLocale(Locale jsfLocale) {
        if (!Strings.isNullOrEmpty(variant)) {
            return new Locale(language, country, variant);
        } else if (!Strings.isNullOrEmpty(country)) {
            return new Locale(language, country);
        } else if (!Strings.isNullOrEmpty(language)) {
            return new Locale(language);
        } else {
            return jsfLocale;
        }
    }

    public String getLocaleString() {
        return getLocale().toString();
    }

    public void setLocaleString(String localeString) {
        StringTokenizer tokens = new StringTokenizer(localeString, "-_");
        language = tokens.hasMoreTokens() ? tokens.nextToken() : null;
        country = tokens.hasMoreTokens() ? tokens.nextToken() : null;
        variant = tokens.hasMoreTokens() ? tokens.nextToken() : null;
        
        setLocale(calculateLocale(FacesContext.getCurrentInstance().getViewRoot().getLocale()));
    }

    public Locale getLocale() {
        return locale != null ? locale : FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        CookieUtils.setCookie(LOCALE_COOKIE, locale.toString(), -1);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    /**
     * JSF Ayarlarına konmuş olan desteklenen dilleri döndürür.
     *
     * @return
     */
    public List<SelectItem> getSupportedLocales() {

        List<SelectItem> selectItems = new ArrayList<>();

        Iterator<Locale> locales = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();

        while (locales.hasNext()) {

            Locale locale = locales.next();

            if (!Strings.isNullOrEmpty(locale.getLanguage())) {
                selectItems.add(new SelectItem(locale.toString(), locale.getDisplayName(locale)));
            }
        }
        return selectItems;
    }

    /**
     * CDI Bean Intance'i döndürür.
     *
     * @return
     */
    public static LocaleSelector instance() {
        return BeanProvider.getContextualReference(LocaleSelector.class, true);
    }

}
