/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Normal Text Out veren column tipi.
 * 
 * @author Hakan Uygun
 * @param <E> Entitity Sınıfı
 */
public class MessageColumn<E> extends Column<E>{

    public MessageColumn(SingularAttribute<? super E, ?> attribute, String labelKey, String keyPrefix) {
        super(attribute, labelKey);
        setKeyPrefix(keyPrefix);
    }

    
    @Override
    public String getTemplate() {
        return "messageColumn";
    }
    
}
