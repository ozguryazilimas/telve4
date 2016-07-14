/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * outputFeatureLink composit bileşeni tarafından action controller olarak kullanılır.
 * 
 * @author Hakan Uygun
 */
@Named
@RequestScoped
public class FeatureLinkController {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeatureLinkController.class);
    
    public Class<? extends ViewConfig> go( String feature, Long id ){
        FeatureHandler handler = FeatureRegistery.getHandler(feature);
        if( handler != null ){
            return handler.goView(id);
        }
        //Eğer bulamaz ise hiç bir yere oynamıyoruz. Ama bir log atıyoruz.
        LOG.warn("Feature {} connot find. So Feature link not worked", feature);
        return null;
    }
    
}
