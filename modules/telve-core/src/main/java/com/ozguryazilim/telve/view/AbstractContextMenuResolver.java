package com.ozguryazilim.telve.view;

import java.io.Serializable;
import java.util.List;

/**
 * ContextMenuResolver'lar için taban sınıf.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractContextMenuResolver implements Serializable{
    
    /**
     * Verilen ID için fragment listesini hazırlayıp doldurur.
     * 
     * @param viewId
     * @param list 
     */
    public abstract void buildContextMenuFragments( String viewId, List<String> list );
    
}
