package com.ozguryazilim.telve.config;

import java.util.Map.Entry;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OptionPane'leri kullanarak kullanıcıların sistem ayarlarını tanımlaması için
 * UI kontrol sınıfı.
 *
 * @author Hakan Uygun
 */
@WindowScoped
@Named
public class OptionPaneController extends AbstractOptionPaneController {

    private static final Logger LOG = LoggerFactory.getLogger(OptionPaneController.class);
    
    @Inject
    @Any
    private Subject identity;

    @Override
    protected void buildPaneList() {
        for (Entry<String, OptionPane> e : OptionPaneRegistery.getOptionPanes().entrySet()) {

            //User tipindeki paneler ile ilgileniyoruz.
            if (e.getValue().type() == OptionPaneType.User) {
                //Permission tanımlı değişse sınıf üzerinden bakıyoruz.
                String p = e.getValue().permission();
                if (p.isEmpty()) {
                    p = e.getKey();
                }

                //OptionPane exclude edildi mi?
                Boolean excluded = "true".equals(ConfigResolver.getPropertyValue("optionPane.exclude." + e.getKey(), "false"));
                LOG.debug( "OptionPane {}  excluded : {}", e.getKey(), excluded);
                
                //Yetki kontrolü yapalım bakalım
                if ( !excluded && ( p.equals("PUBLIC") || identity.isPermitted(p + ":select"))) {
                    getOptionPanes().add(e.getKey());

                    String viewId = getOptiponPageViewId(e.getValue().optionPage());

                    getOptionPaneViews().put(e.getKey(), viewId);

                }
            }
        }
    }

}
