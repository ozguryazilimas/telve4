package com.ozguryazilim.telve.data;

import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase_;
import java.util.ArrayList;
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
                .or(
                    criteria().like( TreeNodeEntityBase_.code, "%" + searchText + "%"),
                    criteria().like( TreeNodeEntityBase_.name, "%" + searchText + "%")
                 )
                .eq(TreeNodeEntityBase_.active, true)
                .getResultList();
    }
    
    @Override
    public List<E> suggestion(String searchText) {
        return criteria()
                .or(
                    criteria().like( TreeNodeEntityBase_.code, "%" + searchText + "%"),
                    criteria().like( TreeNodeEntityBase_.name, "%" + searchText + "%")
                 )
                .eq(TreeNodeEntityBase_.active, true)
                .getResultList();
    }
    
    
    @Override
    public List<E> suggestionLeaf(String searchText) {
        //FIXME: Bu işlem aslında veri tabanı seviyesinde exist ile yapılsa daha şahane olur : 
        // select * from TREE_TABLE o
        // where not EXISTS ( select * from TREE_TABLE o2 where o2.PID = o.ID )
        //
        List<E> res = new ArrayList<>();
        List<E> ls = criteria()
                .or(
                    criteria().like( TreeNodeEntityBase_.code, "%" + searchText + "%"),
                    criteria().like( TreeNodeEntityBase_.name, "%" + searchText + "%")
                 )
                .eq(TreeNodeEntityBase_.active, true)
                .getResultList();
        
        
        //Child nodelar varsa listeden çıkar
        for( E e : ls ){
            if( findNodes(e).isEmpty() ){
                res.add(e);
            }
        }
        
        
        return res;
    }

    @Override
    public List<E> findAllActives() {
        return criteria()
                .eq(TreeNodeEntityBase_.active, true)
                .getResultList();
    }
    
    /**
     * Geriye root node listesini döndürür.
     * @return 
     */
    public List<E> findRootNodes() {
        return criteria()
                .isNull(TreeNodeEntityBase_.parent)
                //.eq(TreeNodeEntityBase_.active, true)
                .getResultList();
    }
    
    /**
     * Ağac sırasına göre veri listesini döndürür.
     * @return 
     */
    public List<E> findNodes() {
        return criteria()
                //.eq(TreeNodeEntityBase_.active, true)
                .orderAsc(TreeNodeEntityBase_.path)
                .getResultList();
    }
    
    
    /**
     * Verilen parent altındaki node listesini döndürür.
     * @return 
     */
    public List<E> findNodes( E parent ) {
        return criteria()
                .eq(TreeNodeEntityBase_.parent, parent)
                .orderAsc(TreeNodeEntityBase_.path)
                .getResultList();
    }
    
    
    public abstract List<E> findByCode( String code );
}
