package com.ozguryazilim.telve.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Context Menu fragment resolver.
 * 
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class ContextMenuResolver implements Serializable{
   
    
    private Map<String, List<String>> contextCache = new HashMap<>();
    
    @Inject
    private FacesContext facesContext;
    
    public List<String> getContextMenuFragments( ){
        return getContextMenuFragments(facesContext.getViewRoot().getViewId());
    }
    
    /**
     * Verilen ViewID için include alınacak fragment id listesini döndürür.
     * 
     * @param viewId
     * @return 
     */
    public List<String> getContextMenuFragments( String viewId ){
        
        if( !contextCache.containsKey(viewId) ){
            buildContextMenu( viewId );
        }
        
        if( !contextCache.containsKey(viewId) ){
            return Collections.EMPTY_LIST;
        }
        
        return contextCache.get(viewId);
    }

    /**
     * Verilen ID için resolve işlemini yapıp session cache'e koyar.
     * @param viewId 
     */
    private void buildContextMenu( String viewId ) {
        
        List<String> list = new ArrayList<>();
        
        
        List<String> ls = ContextMenuResolverRegistery.getMenuList(viewId);
        if( ls != null ){
            list.addAll(ls);
        }
        
        for( AbstractContextMenuResolver resolver : ContextMenuResolverRegistery.getResolvers() ){
            resolver.buildContextMenuFragments(viewId, list);
        }
        
        contextCache.put(viewId, list);
        
    }
    
    
}
