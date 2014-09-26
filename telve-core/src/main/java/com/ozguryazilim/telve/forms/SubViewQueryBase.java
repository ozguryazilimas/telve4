/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.query.QueryControllerBase;

/**
 * SubView'lar için detay soru control sınıfı.
 * 
 * @author Hakan Uygun
 * @param <E> Sorgu için kullanılacak Entity Sınıfı
 * @param <R> Sorgu sonuçları için kullanılacak ViewModel
 */
public abstract class SubViewQueryBase<E extends EntityBase,R extends ViewModel> extends QueryControllerBase<E,R>{

    
}
