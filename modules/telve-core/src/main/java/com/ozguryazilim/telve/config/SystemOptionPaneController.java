package com.ozguryazilim.telve.config;

import java.util.Map;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author oyas
 */
@WindowScoped
@Named
public class SystemOptionPaneController extends AbstractOptionPaneController {

    @Inject
    @Any
    private Subject identity;

    @Override
    protected void buildPaneList() {
        for (Map.Entry<String, OptionPane> e : OptionPaneRegistery.getOptionPanes().entrySet()) {

            //Sistem için pane'ler ile ilgileniyoruz
            if (e.getValue().type() == OptionPaneType.System) {

                //Permission tanımlı değişse sınıf üzerinden bakıyoruz.
                String p = e.getValue().permission();
                if (p.isEmpty()) {
                    p = e.getKey();
                }

                if (identity.isPermitted(p + ":select")) {
                    getOptionPanes().add(e.getKey());

                    String viewId = getOptiponPageViewId(e.getValue().optionPage());

                    getOptionPaneViews().put(e.getKey(), viewId);

                }
            }
        }
    }

}
