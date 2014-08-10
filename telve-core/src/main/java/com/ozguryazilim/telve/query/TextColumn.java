/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Normal Text Out veren column tipi.
 * 
 * @author Hakan Uygun
 * @param <E> Entitity Sınıfı
 */
public class TextColumn<E> extends Column<E>{

    public TextColumn(SingularAttribute<E, ?> attribute, String labelKey) {
        super(attribute, labelKey);
    }

    
    @Override
    public String getTemplate() {
        return "column";
    }
    
}
