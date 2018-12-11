/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.entities.TreeNodeModel;
import com.ozguryazilim.telve.forms.RefreshBrowserEvent;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

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
public abstract class LookupTreeControllerBase<E extends TreeNodeEntityBase, F extends TreeNodeModel> extends LookupControllerBase<E, F> implements TreeNodeTypeSelector<E> {
    
        @Override
    protected void initModel() {
        LookupTreeModel<F> m = new LookupTreeModel<>();
        m.setTypeSelector(this);
        setModel(m);
    }

    @Override
    public void search() {
        populateData();
    }

    
    @Override
    public void populateData() {
        if( getModel().isDataEmpty() ){
            List<F> data = getRepository().lookupQuery("");
            getModel().setData(data);
        }
        ((LookupTreeModel)getModel()).buildResultList();
    }
    
    /**
     * Ağaçlar için geriye verilen serinin parent bilgilerinin de dolu olduğu bir veri set döndürür.
     * 
     * Özellikle arama sonuçlarının ağaç görünümünde parent bilgilerinin de gösterilmesi için kulanılır.
     * 
     * @param data
     * @return 
     */
    public List<F> populateParentData( List<F> data ){
        
        //Todo: Burada performans için path tabanlı bir çözüm daha makul olacaktır.
        
        List<F> result = new ArrayList<>();
        
        for( F f : data ){
            if( !result.contains(f.getParent()) && !data.contains(f.getParent()) && f.getParentId() > 0){
                result.add( (F)getRepository().findBy(f.getParentId()) );
            }
        }

        result.addAll(data);
        
        return result;
    }

    @Override
    public String getNodeType(E node) {
        return "default";
    }
    
    /**
     * RefreshBrowseEvent'i dinlenir ve ilgili domain ise search komutu çalıştırılır.
     * @param event 
     */
    public void refreshListener( @Observes(notifyObserver = Reception.IF_EXISTS) RefreshBrowserEvent event ){
        if( event.getDomain().equals( getRepository().getEntityClass().getName()) ){
            if( !getModel().isDataEmpty() ){
                getModel().clearData();
                search();
            }
        }
    }
}
