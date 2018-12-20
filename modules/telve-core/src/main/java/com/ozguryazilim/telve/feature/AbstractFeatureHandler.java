package com.ozguryazilim.telve.feature;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.entities.ViewModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameterContext;

/**
 * Abstract FeturHandler
 * 
 * Altsınıflarda minimumkod yazılması için
 * 
 * @author Hakan Uygun
 */
public class AbstractFeatureHandler implements Serializable, FeatureHandler{

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private NavigationParameterContext navigationParameterContext;
    
    
    private Set<FeatureCapability> capabilities = new HashSet<>();
    private Map<PageType, Class<? extends ViewConfig>> pageMap = new HashMap<>();
    
    
    @PostConstruct
    public void init(){
        initCapabilities();
        //Cahce ViewConfigs
    }
    
    
    /**
     * Sınıf üzerindeki annotationlara bakarak bu feature'ın ne gibi yetenekleri olduğunun listesini çıkarır.
     * Bu arada gene aynı annotation'lar üzerinde bulunan bilgilerin bi rkısmını cachler ( page'ler )
     */
    protected void initCapabilities(){
        
        Page[] pages = getClass().getAnnotationsByType(Page.class);
        
        for( Page p : pages ){
            //FIXME: Param ve Lookup'lar için düşünelim
            switch( p.type() ){
                case BROWSE : 
                    capabilities.add(FeatureCapability.HAS_BROWSE);
                    pageMap.put(PageType.BROWSE, p.page());
                    break;
                    
                case EDIT : 
                    capabilities.add(FeatureCapability.HAS_EDIT);
                    pageMap.put(PageType.EDIT, p.page());
                    break;
                    
                case VIEW : 
                    capabilities.add(FeatureCapability.HAS_VIEW);
                    pageMap.put(PageType.VIEW, p.page());
                    break;
            }
        }

        //TODO: GlobalSearch ve QuickCommand için de burada capability taraması yapılacak
        
    }
    
    /**
     * Bu sınıf override edilip HomeBean'in actionları çağrılabilir.
     * @param id
     * @return 
     */
    @Override
    public Class<? extends ViewConfig> goView(Long id) {
        navigationParameterContext.addPageParameter("eid", id);
        return getViewPage();
    }
    
    
    /**
     * Geriye view için tanımlanmış Page( type=VIEW, ... ) sayfa bilgisini döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getViewPage() {
        return pageMap.get(PageType.VIEW);
    }
    
    /**
     * Geriye browse için tanımlanmış Page( type=BROWSE, ... ) sayfa bilgisini döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getBrowsePage() {
        return pageMap.get(PageType.BROWSE);
    }
    
    /**
     * Geriye edit için tanımlanmış Page( type=EDIT, ... ) sayfa bilgisini döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getEditPage() {
        return pageMap.get(PageType.EDIT);
    }

    @Override
    public Boolean hasCapability(FeatureCapability capability) {
        return capabilities.contains(capability);
    }

    @Override
    public FeatureLink getFeatureLink(ViewModel entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCaption() {
        String c = getClass().getAnnotation(Feature.class).caption();
        if( Strings.isNullOrEmpty(c)){
            c = "feature.caption." + getName();
        }
        return c;
        
    }
    
    @Override
    public String getName() {
        //String result = getClass().getAnnotation(Feature.class).name();
        //if( Strings.isNullOrEmpty(result)){
        //    result = getClass().getSimpleName();
        //}
        return getClass().getSimpleName();
    }
}
