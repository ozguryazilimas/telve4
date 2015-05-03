/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Liste detaylarını gösteren kolon tipi
 * @author Hakan Uygun
 * @param <E>
 */
public class ListColumn<E, F, G> extends Column<E>{

    private SingularAttribute<? super F, ?> itemAttribute;
    private SingularAttribute<? super G, ?> subAttribute;
    
    public ListColumn(ListAttribute<? super E, ?> attribute, SingularAttribute<? super F, ?> itemAttribute, SingularAttribute<? super G, ?> subAttribute, String labelKey) {
        super(attribute, labelKey);
        this.itemAttribute = itemAttribute;
        this.subAttribute = subAttribute;
    }

    @Override
    public String getTemplate() {
        return "listColumn";
    }

    public SingularAttribute<? super F, ?> getItemAttribute() {
        return itemAttribute;
    }

    public void setItemAttribute(SingularAttribute<? super F, ?> itemAttribute) {
        this.itemAttribute = itemAttribute;
    }

    public SingularAttribute<? super G, ?> getSubAttribute() {
        return subAttribute;
    }

    public void setSubAttribute(SingularAttribute<? super G, ?> subAttribute) {
        this.subAttribute = subAttribute;
    }

    public String getItemName(){
        return getItemAttribute().getName();
    }
    
    public String getSubName(){
        return getSubAttribute().getName();
    }
    
    
    
}
