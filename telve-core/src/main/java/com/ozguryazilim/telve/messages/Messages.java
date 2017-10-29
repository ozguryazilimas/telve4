/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messages;

import com.google.common.base.Splitter;
import com.ozguryazilim.telve.config.LocaleSelector;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Seam 2 biçminde Messages üretir.
 * 
 * Kod Seam 2 den kopyalandı
 * 
 * @author Hakan Uygun
 */
@Dependent
public class Messages {

    protected Map<String, String> createMap( final Locale locale) {
        final java.util.ResourceBundle bundle = TelveResourceBundle.getBundle(locale);
        //final MessageBundles bundle = MessageBundles.getInstance();

        if (bundle == null) {
            return null;
        }

        return new AbstractMap<String, String>() {
            @Override
            public String get(Object key) {
                if (key instanceof String) {
                    String resourceKey = (String) key;

                    String resource;
                    try {
                        resource = bundle.getString(resourceKey);
                    } catch (MissingResourceException mre) {
                        return resourceKey;
                    }

                    return (resource == null) ? resourceKey : resource;

                } else {
                    return null;
                }
            }

            @Override
            public Set<Map.Entry<String, String>> entrySet() {
                //Bu method özellikle serializable yapan yerler ( caml mesela ) için gerekiyor
                Set<Map.Entry<String, String>> entrySet = new HashSet<>();
                
                
                Enumeration<String> keys = bundle.getKeys();

                while (keys.hasMoreElements()) {
                    final String key = keys.nextElement();

                    entrySet.add(new Map.Entry<String, String>() {

                        public String getKey() {
                            return key;
                        }

                        public String getValue() {
                            return get(key);
                        }

                        public String setValue(String arg0) {
                            throw new UnsupportedOperationException("not implemented");
                        }
                    });
                }
                
                return entrySet;
                
            }

        };

    }

    /**
     * Create the Map and cache it in the Request scope. No need to cache it in
     * the SESSION scope, since it is inexpensive to create.
     *     
     * @return a Map that interpolates messages in the Telve MessageBundles
     */
    @Produces @RequestScoped @Named("messages")
    public Map<String, String> procuceMessages() {
        return createMap(getCurrentLocale());
    }

    public static Locale getCurrentLocale() {
        
        Locale l;
        try{
            //Oturum varsa kullanıcı dil seçimini al
            l = LocaleSelector.instance().getLocale();
        } catch ( ContextNotActiveException e ){
            //Session'a sahip olmayan bir yerden çağrılırsa eğer uygulama'nın değerini al.
            //Mesela camel şeysilerinden.
            l = new Locale(ConfigResolver.getPropertyValue("application.locale", "tr"));
        }
        return l != null ? l : Locale.getDefault();
        
    }
    
    //***** Static Util Functions ****//
    public static Map<String, String> getMessages() {
        return new Messages().procuceMessages();
    }
    
    public static Map<String, String> getMessages( Locale locale ) {
        return new Messages().createMap(locale);
    }
    
    public static Map<String, String> getMessages( String locale ) {
        return new Messages().createMap(new Locale(locale));
    }
    
    public static String getMessage( String key ) {
        return getMessages( getCurrentLocale() ).get(key);
    }
    
    public static String getMessage( Locale locale, String key ) {
        return getMessages( locale ).get(key);
    }
    
    public static String getMessage( String key, Object... args ) {
        return getMessage( getCurrentLocale(), key, args );
    }
    
    public static String getMessage( Locale locale, String key, Object... args ) {
        String pattern = getMessages( locale ).get(key);
        
        MessageFormat formatter = new MessageFormat(pattern, locale );
        return formatter.format(args);
    }
    
    /**
     * Tek bir string içinde hem format pattenKey'i hem de argüman listesi olan bir veriyi parse eder.
     * 
     * Veriler $%& karakterleri ile ayrılırlar.
     * 
     * Data formatı : hello$%&Telve    //Birinci parça i18n anahtar, ikinci parça args 0
     * Dil dosyası  : hello=Hello {0}  //MessageFormat sınıfının kuralları
     * 
     * @param locale
     * @param data
     * @return 
     */
    public static String getMessageFromData( Locale locale, String data ) {
        List<String> ls = Splitter.on("$%&").trimResults().omitEmptyStrings().splitToList(data);
        if( ls.isEmpty() ) return "";
        
        String patternKey = ls.get(0);
        String pattern = getMessages( locale ).get(patternKey);
        
        String[] params = new String[ls.size()-1];
        for( int i = 1; i < ls.size(); i++ ){
            params[i-1] = ls.get(i);
        }
        
        MessageFormat formatter = new MessageFormat(pattern, locale );
        return formatter.format(params);
    }
}
