package com.ozguryazilim.telve.messages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Messages formated değerlerini JSF tarafından erişilebilir kılmak için.
 * 
 * Uygulama içerisinde doğrudan Messages sınıfının karşılıkları kullanılabilir.
 * 
 * @author Hakan Uygun
 */
@RequestScoped
@Named
public class FormatedMessage {
    
    public String getFormatedMessage( String key, Object... o ){
        return Messages.getMessage(Messages.getCurrentLocale(), key, o);
    }
    
    public String getMessageFromData( String data ){
        return Messages.getMessageFromData(Messages.getCurrentLocale(), data );
    }
}
