/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Link üretecek column tipi
 * @author Hakan Uygun
 * @param <E>
 */
public class LinkColumn<E> extends Column<E>{
    
    public LinkColumn( SingularAttribute<? super E, ?> attribute, String labelKey ){
        super(attribute, labelKey);
    }

    @Override
    public String getTemplate() {
        return "linkcolumn";
    }
    
}
