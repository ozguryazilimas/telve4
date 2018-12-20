package com.ozguryazilim.telve.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Kullanıcı ve ProjectStage gön önünde bulundurarak Sistem içinde tanımlı
 * konfig bilgilerini ayarlar.
 *
 * DeltaSpike ConfigResolver üzerine Veri tabanını kullanır.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class TelveConfigResolver {

    
    public String getProperty(String key) {

        String account = "";
        //Ortalıkta bir session olmadığı durumlarda patlıyor. Dolayısı ile çalışmıyor. Zaten DB kısmısı da çalışmıyor.
        //if ( userInfo != null ) {
        //    account = userInfo.getLoginName();
        //}

        String result = ConfigResolver.getPropertyValue(account + "." + key);

        if (result == null) {
            result = ConfigResolver.getPropertyValue(key);
        }

        return result;
    }

    public String getProperty(OptionKey key) {
        return getProperty(key.getValue());
    }
}
