/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.forms.SubViewFilter;
import com.ozguryazilim.telve.idm.config.IdmPages;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;

/**
 * Eğer çoklu grup kullanımı yoksa UserGroupSubView filtrelenecek
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
public class UerGroupSubViewFilter implements SubViewFilter {

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Override
    public void filter(Class<? extends ViewConfig> container, List<String> views) {
        if (IdmPages.UserView.class == container) {
            if (!"true".equals(ConfigResolver.getPropertyValue("security.multiGroup.control", "false"))) {
                views.remove(viewConfigResolver.getViewConfigDescriptor(IdmPages.UserGroupSubView.class).getViewId());
            }
        }
    }

}
