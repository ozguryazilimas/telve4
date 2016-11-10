/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messages;

import com.google.common.base.Splitter;
import com.ozguryazilim.telve.config.LocaleSelector;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Dil dosyasından format patterni ile gelen bir değeri parametreleri verilerek 
 * @author Hakan Uygun
 */
@RequestScoped
@Named
public class FormatedMessage {
    
    
    @Inject @Named("messages")
    private Map<String,String> messages;
    
    public String getMessage( String pattern, Object[] o ){
        MessageFormat formatter = new MessageFormat(pattern, getLocale());
        return formatter.format(o);
    }
    
    public String getMessageFromData( String data ){
        
        List<String> ls = Splitter.on("$%&").trimResults().omitEmptyStrings().splitToList(data);
        if( ls.isEmpty() ) return "";
        
        String patternKey = ls.get(0);
        String pattern = messages.get(patternKey);
        
        String[] params = new String[ls.size()-1];
        for( int i = 1; i < ls.size(); i++ ){
            params[i-1] = ls.get(i);
        }
        
        return getMessage(pattern, params);
        
    }
    
    public Locale getLocale() {
        
        Locale l;
        try{
            l = LocaleSelector.instance().getLocale();
        } catch ( ContextNotActiveException e ){
            //Session'a sahip olmayan bir yerden çağrılırsa eğer uygulama'nın değerini al.
            //Mesela camel şeysilerinden.
            l = new Locale(ConfigResolver.getPropertyValue("application.locale", "tr"));
        }
        return l != null ? l : Locale.getDefault();
        
    }
}
