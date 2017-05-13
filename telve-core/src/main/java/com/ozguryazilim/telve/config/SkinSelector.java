/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.config;

import com.ozguryazilim.telve.utils.CookieUtils;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Telve UI için Skin Selector
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class SkinSelector implements Serializable{
    
    private static final String SKIN_COOKIE = "telve.skin";
    private static final String SIDEBAR_COOKIE = "telve.sm";
    private static final String SLIMSCROLL_COOKIE = "telve.ss";
    private static final String SIDEBAR_COLLAPSE_COOKIE = "telve.sc";
    
    private String skin;
    private Boolean sidebarMini;
    private Boolean slimScroll;
    private Boolean sidebarCollapse;
    
    public String getSkin(){
        if (skin == null) {

            //Önce Cookie var mı diye bakalım.
            Cookie c = CookieUtils.getCookie(SKIN_COOKIE);
            if (c != null) {
                skin = c.getValue();
            } else {
                String t = ConfigResolver.getPropertyValue("theme.skin.name", "skin-blue");
                setSkin( t );
            }
        }
        return skin;
    }
    
    public void setSkin( String skin ){
        this.skin = skin;
        CookieUtils.setCookie(SKIN_COOKIE, skin, CookieUtils.EXPIRE_IN_TEN_YEAR);
    }
    
    public Boolean getSidebarMini(){
        if (sidebarMini == null) {

            //Önce Cookie var mı diye bakalım.
            Cookie c = CookieUtils.getCookie(SIDEBAR_COOKIE);
            if (c != null) {
                sidebarMini = "true".equals( c.getValue() );
            } else {
                String t = ConfigResolver.getPropertyValue("theme.sidebarmini", "false");
                setSidebarMini( "true".equals(t) );
            }
        }
        return sidebarMini;
    }
    
    public void setSidebarMini( Boolean sidebarMini ){
        this.sidebarMini = sidebarMini;
        CookieUtils.setCookie(SIDEBAR_COOKIE, sidebarMini ? "true" : "false", CookieUtils.EXPIRE_IN_TEN_YEAR);
    }
    
    public Boolean getSlimScroll(){
        if (slimScroll == null) {

            //Önce Cookie var mı diye bakalım.
            Cookie c = CookieUtils.getCookie(SLIMSCROLL_COOKIE);
            if (c != null) {
                slimScroll = "true".equals( c.getValue() );
            } else {
                String t = ConfigResolver.getPropertyValue("theme.slimScroll", "true");
                setSlimScroll( "true".equals(t) );
            }
        }
        return slimScroll;
    }
    
    public void setSlimScroll( Boolean slimScroll ){
        this.slimScroll = slimScroll;
        CookieUtils.setCookie(SLIMSCROLL_COOKIE, slimScroll ? "true" : "false", CookieUtils.EXPIRE_IN_TEN_YEAR);
    }
    
    
    public String getSidebarCSS(){
        return getSidebarMini() ? "sidebar-mini sidebar-collapse" : "";
    }
}
