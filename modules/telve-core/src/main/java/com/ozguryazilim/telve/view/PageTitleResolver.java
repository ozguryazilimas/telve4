package com.ozguryazilim.telve.view;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;

/**
 * @PageTitle ile annotate edilmiş view'ler için title döndürür.
 * 
 * @author Hakan Uygun 
 */
@Named
@RequestScoped 
public class PageTitleResolver {
   
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private FacesContext facesContext;
    
    /**
     * Verilen view ID DeltaSpike ViewConfig kapsamında ve PageTitle ile işaretlenmişse title döndürür.
     * 
     * Eğer birden fazla PageTitle ile işaretlenmişse işlkini döndürür.
     * 
     * Eğer hiç işaretlenmemişse sınıf adını döndürür.
     * 
     * @param viewId
     * @return 
     */
    public String getPageTitle( String viewId ){
        
        ViewConfigDescriptor vcd = viewConfigResolver.getViewConfigDescriptor( viewId );
        
        if( vcd != null ){
            List<PageTitle> ls = vcd.getMetaData(PageTitle.class);
            
            if( !ls.isEmpty() ){
                return ls.get(0).value();
            }
            
            return "page.title." + vcd.getConfigClass().getSimpleName();
        }
        
        return "";
    }
    
    /**
     * Mevcut View için DeltaSpike ViewConfig kapsamında ve PageTitle ile işaretlenmişse title döndürür.
     * @return 
     */
    public String getPageTitle(){
        return getPageTitle( facesContext.getViewRoot().getViewId());
    }
    
}
