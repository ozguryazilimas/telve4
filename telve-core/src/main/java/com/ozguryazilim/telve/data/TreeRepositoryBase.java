/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.data;

import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase_;
import java.util.List;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Tree Veri Modelleri için Repository Base
 * @author Hakan Uygun
 * @param <E> Repository için temel Entity Sınıfı
 */
public abstract class TreeRepositoryBase<E extends TreeNodeEntityBase> extends RepositoryBase<E, E> implements CriteriaSupport<E>{

    
    /**
     * Lookuplar tarafından kullanılan sorgu.
     *
     * TreeNode'ları için code ve ad üzerinden like ile arama yapar.
     *
     * @param searchText
     * @return
     */
    @Override
    public List<E> lookupQuery(String searchText) {
        return criteria()
                .like( TreeNodeEntityBase_.code, "%" + searchText + "%")
                .getResultList();
    }
    
    @Override
    public List<E> suggestion(String searchText) {
        return criteria()
                .like(TreeNodeEntityBase_.code, "%" + searchText + "%")
                .getResultList();
    }
    
}
