package com.ozguryazilim.telve.link;

import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Link Resolverlar için taban sınıf.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractLinkResolver {
    
    /**
     * Geriye domain adını döndürür.
     * 
     * Genelde entity sınıf adı olur.
     * 
     * @return 
     */
    public abstract String getDomainName();
    
    /**
     * Geriye domain ismi için message key döndürür.
     * @return 
     */
    public abstract String getDomainCaption();
    
    /**
     * Geriye gösterim yapılacak olan view ID döner.
     * @return 
     */
    public abstract String getViewId();
    
    public String getIcon(){
        return "";
    }
    
    public String getUrl( Long pk ){
        String s = getViewId() + "?eid=" + pk;
        return s;
    }
    
    public Link resolve( String caption, Long pk ){
        Link l = new Link( getDomainCaption(), caption, getUrl( pk ), getIcon(), pk, getViewId());
        return l;
    }
    
    protected ViewConfigResolver getViewConfigResolver(){
        return BeanProvider.getContextualReference(ViewConfigResolver.class);
    }
}
