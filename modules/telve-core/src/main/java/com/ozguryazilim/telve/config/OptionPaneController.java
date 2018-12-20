package com.ozguryazilim.telve.config;

import java.util.Map.Entry;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.shiro.subject.Subject;

/**
 * OptionPane'leri kullanarak kullanıcıların sistem ayarlarını tanımlaması için
 * UI kontrol sınıfı.
 *
 * @author Hakan Uygun
 */
@WindowScoped
@Named
public class OptionPaneController extends AbstractOptionPaneController {

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

                //Herkes kendi arayüzünü değiştirebilir.
                if (p.equals("PUBLIC") || identity.isPermitted(p + ":select")) {
                    getOptionPanes().add(e.getKey());

                    String viewId = getOptiponPageViewId(e.getValue().optionPage());

                    getOptionPaneViews().put(e.getKey(), viewId);

                }
            }
        }
    }

}
