/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.view;

import java.util.List;
import javax.enterprise.context.RequestScoped;
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
    
    /**
     * Verilen view ID DeltaSpike ViewConfig kapsamında ve PageTitle ile işaretlenmişse title döndürür.
     * 
     * Eğer birden fazla PageTitle ile işaretlenmişse işlkini döndürür.
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
        }
        
        return "";
    }
    
}
