/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
