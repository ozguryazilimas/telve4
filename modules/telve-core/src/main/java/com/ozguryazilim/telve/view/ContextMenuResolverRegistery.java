/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Context Menu Resolver'lar için registery
 * 
 * @author Hakan Uygun
 */
public class ContextMenuResolverRegistery {
    
    private static List<AbstractContextMenuResolver> resolvers = new ArrayList<>();
    
    private static Map<String, List<String>> contextMenuMap = new HashMap<>();
    
    /**
     * Verilen resolver'ı register eder.
     * @param resolver 
     */
    public static void register( AbstractContextMenuResolver resolver ){
        resolvers.add(resolver);
    }
    
    
    /**
     * Verilen viewId için verilen fragment'i register eder.
     * @param viewId
     * @param fragment 
     */
    public static void registerMenu( String viewId, String fragment ){
        
        List<String> ls = contextMenuMap.get(viewId);
        if( ls == null ){
            ls = new ArrayList<>();
            contextMenuMap.put(viewId, ls);
        }
        
        ls.add(fragment);
    }
    
    public static void registerMenu( Class<? extends ViewConfig> viewConfig, String fragment ){
        
        ViewConfigResolver viewConfigResolver = BeanProvider.getContextualReference(ViewConfigResolver.class, false);
        
        String viewId = viewConfigResolver.getViewConfigDescriptor(viewConfig).getViewId();
                
        registerMenu(viewId, fragment);
    }
    
    
    public static void registerMenu( Class<? extends ViewConfig> viewConfig, Class<? extends ViewConfig> fragmentConfig ){
        
        ViewConfigResolver viewConfigResolver = BeanProvider.getContextualReference(ViewConfigResolver.class, false);
        
        String viewId = viewConfigResolver.getViewConfigDescriptor(viewConfig).getViewId();
        String fragment = viewConfigResolver.getViewConfigDescriptor(fragmentConfig).getViewId();
                
        registerMenu(viewId, fragment);
    }
    
    /**
     * Tanımlanmış resolver listesini döndürür.
     * @return 
     */
    public static List<AbstractContextMenuResolver> getResolvers(){
        return resolvers;
    }
    
    /**
     * Verilen ViewId için tanımlı menu fragment listesini döndürür.
     * 
     * Eğer viewid için bir liste yoksa null döner.
     * @param viewId
     * @return 
     */
    public static List<String> getMenuList( String viewId ){
        return contextMenuMap.get(viewId);
    }
}
