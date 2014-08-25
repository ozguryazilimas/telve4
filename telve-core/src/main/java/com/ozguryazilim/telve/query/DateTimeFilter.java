/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Tarih / Saat Filtresi.
 * 
 * @author Hakan Uygun
 * @param <E> Filtrenin uygulanacağı Entity sınıfı
 */
public class DateTimeFilter<E> extends DateFilter<E>{

    public DateTimeFilter(SingularAttribute<? super E, Date> attribute, String label) {
        super(attribute, label);
    }

    @Override
    public String getTemplate() {
        return "dateTimeFilter";
    }
    
    
}
