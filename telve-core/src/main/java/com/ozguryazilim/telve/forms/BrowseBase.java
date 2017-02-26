/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.query.QueryControllerBase;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.feature.Feature;
import com.ozguryazilim.telve.feature.FeatureHandler;
import com.ozguryazilim.telve.feature.Page;
import com.ozguryazilim.telve.feature.PageType;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameterContext;
import org.apache.deltaspike.core.api.literal.AnyLiteral;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ProxyUtils;

/**
 * Browse sınıfları için taban.
 *
 * @author Hakan Uygun
 * @param <E>
 * @param <R>
 */
public abstract class BrowseBase<E extends EntityBase, R extends ViewModel> extends QueryControllerBase<E, R> {

    @Inject
    private Identity identity;
    
    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    private NavigationParameterContext navigationParameterContext;

    public Class<? extends ViewConfig> edit(Long id) {
        navigationParameterContext.addPageParameter("eid", id);
        return getEditPage();
    }

    public Class<? extends ViewConfig> view(Long id) {
        navigationParameterContext.addPageParameter("eid", id);
        return getContainerViewPage();
    }

    public String getContainerViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getContainerViewPage()).getViewId();
    }

    public String getEditViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getEditPage()).getViewId();
    }

    /**
     * Geriye feature ile verilmiş olan permission domain bilgisini döndürür.
     *
     * @return
     */
    public String getPermissionDomain() {
        return this.getClass().getAnnotation(Browse.class).feature().getAnnotation(Feature.class).permission();
    }

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış BrowsePage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getBrowsePage() {
        return findPage(PageType.BROWSE);
    }

    protected Class<? extends ViewConfig> findPage(PageType pageType) {

        Page[] pages = this.getClass().getAnnotation(Browse.class).feature().getAnnotationsByType(Page.class);
        for (Page p : pages) {
            if (p.type() == pageType) {
                return p.page();
            }
        }

        return DefaultErrorView.class;
    }

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış EditPage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getEditPage() {
        return findPage(PageType.EDIT);
        //return this.getClass().getAnnotation(Browse.class).editPage();
    }

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış EditPage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getContainerViewPage() {
        return findPage(PageType.VIEW);
    }

    public Class<? extends FeatureHandler> getFeatureClass(){
        return ((Browse)ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(Browse.class)).feature();
    }
    
    public FeatureHandler getFeature(){
        return BeanProvider.getContextualReference(getFeatureClass(), false, new AnyLiteral());
    }
    
    /**
     * RefreshBrowseEvent'i dinlenir ve ilgili domain ise search komutu
     * çalıştırılır.
     *
     * @param event
     */
    public void refreshListener(@Observes(notifyObserver = Reception.IF_EXISTS) RefreshBrowserEvent event) {
        if (event.getDomain().equals(getRepository().getEntityClass().getName())) {
            search();
        }
    }
    
    public Boolean hasInsertPermission(){
        return identity.hasPermission(getPermissionDomain(), "insert");
    }
}
