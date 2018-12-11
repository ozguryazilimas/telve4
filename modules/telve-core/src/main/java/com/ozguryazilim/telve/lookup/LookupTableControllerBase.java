/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * LookUp Dialogları için taban sınıf.
 *
 * Bu sınıf inherit alınarak gerekli lookup dialogları hazırlanır.
 *
 *
 * Farklı profillere sahip dialog yapılacaksa "profile" bilgisi kullanılmalıdır.
 * Örneğin ContactLookup için "ALL", "CUSTOMER", "CONTACT" gibi değerler
 * olabilir ve bunlar filtreleme sırasında kullanılmalıdır.
 *
 *
 * @author Hakan Uygun
 * @param <T> Kullanılacak olan Entity Sınıfı
 * @param <F> Seçimlerde kullanılacak olan Model sınıfı
 */
public abstract class LookupTableControllerBase<T extends EntityBase, F extends ViewModel> extends LookupControllerBase<T, F> {

    private static final Logger LOG = LoggerFactory.getLogger(LookupTableControllerBase.class);
    
    @Override
    protected void initModel() {
        LookupTableModel<F> m = new LookupTableModel<>();
        buildModel(m);
        setModel(m);
    }

    
    /**
     * Verilen modeli doldurup GUI'de hangi kolonların çıkacağı belirlenir. 
     * @param model 
     */
    protected abstract void buildModel(LookupTableModel<F> model);
    

}
