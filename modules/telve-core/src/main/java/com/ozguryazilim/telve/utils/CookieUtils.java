package com.ozguryazilim.telve.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cookie okuyup yazmak için yardımcı fonksiyonlar.
 *
 * http://stackoverflow.com/questions/20934016/how-to-add-cookie-in-jsf
 *
 * @author Hakan Uygun
 */
public class CookieUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CookieUtils.class);
    
    public static final int EXPIRE_IN_TEN_YEAR = 60 * 60 * 24 * 365 * 10;
    
    public static void setCookie(String name, String value, int expiry) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie cookie = null;

        Cookie[] userCookies = request.getCookies();
        if (userCookies != null && userCookies.length > 0) {
            for (Cookie userCookie : userCookies) {
                if (userCookie.getName().equals(name)) {
                    cookie = userCookie;
                    break;
                }
            }
        }

        String encodedValue = "";
        String b64Value = "";
        try {
            encodedValue = URLEncoder.encode(value, "UTF-8");
            b64Value = Base64.getEncoder().encodeToString(encodedValue.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException | IllegalArgumentException ex) {
            LOG.warn("Cookie cannot write", ex);
        }
        
        if (cookie != null) {
            cookie.setValue(b64Value);
            cookie.setPath(request.getContextPath());
        } else {
            cookie = new Cookie(name, b64Value);
            cookie.setPath(request.getContextPath());
        }

        cookie.setMaxAge(expiry);
        //cookie.setHttpOnly(true);

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.addCookie(cookie);
    }

    public static String getCookie(String name) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if( facesContext == null ) return null;
        
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie cookie = null;

        Cookie[] userCookies = request.getCookies();
        if (userCookies != null && userCookies.length > 0) {
            for (Cookie userCookie : userCookies) {
                if (userCookie.getName().equals(name)) {
                    cookie = userCookie;
                    break;
                }
            }
        }
        
        if( cookie != null ){
            String val = null;
            try {
                String decodedValue =  String.valueOf(Base64.getDecoder().decode(cookie.getValue()));
                val = URLDecoder.decode(decodedValue, "UTF-8");
            } catch (UnsupportedEncodingException | IllegalArgumentException ex) {
                LOG.warn("Cookie cannot read", ex);
            }
            return val;
        } else {
            return null;
        }
        
    }
}
