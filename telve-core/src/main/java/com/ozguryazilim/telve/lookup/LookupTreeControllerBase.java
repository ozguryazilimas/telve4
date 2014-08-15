/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.entities.TreeNodeModel;

/**
 * Ağaç tipi veriler için Lookup Temel sınıfı.
 * 
 * Bu sınıf inherit alınarak gerekli lookup dialogları hazırlanır.
 *
 *
 * Farklı profillere sahip dialog yapılacaksa "profile" bilgisi kullanılmalıdır.
 * Örneğin ContactLookup için "ALL", "CUSTOMER", "CONTACT" gibi değerler
 * olabilir ve bunlar filtreleme sırasında kullanılmalıdır.
 * 
 * @author Hakan Uygun
 * @param <E> Ağaç tipi entity sınıfı
 * @param <F> Sunum için kullanılacak olan model
 */
public abstract class LookupTreeControllerBase<E extends TreeNodeEntityBase, F extends TreeNodeModel> extends LookupControllerBase<E, F>{
    
    private LookupTreeModel<F> model;

    @Override
    protected void initModel() {
        LookupTreeModel<F> m = new LookupTreeModel<>();
        setModel(m);
    }
    
    
    
    
}
