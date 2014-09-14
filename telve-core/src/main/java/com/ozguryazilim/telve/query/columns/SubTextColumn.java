/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Dönen değerin bir alt değerini sunar.
 * 
 * @author Hakan Uygun
 * @param <E> İşlenecek olan Entity Sınıfı
 * @param <F> Alt Entity Sınıfı
 */
public class SubTextColumn<E, F> extends Column<E> {

    private SingularAttribute<? super F, ?> subattribute;
    
    /**
     *
     * @param attribute
     * @param subattribute 
     * @param labelKey
     */
    public SubTextColumn(SingularAttribute<? super E, ?> attribute, SingularAttribute<? super F, ?> subattribute, String labelKey) {
        super(attribute, labelKey);
        this.subattribute = subattribute;
    }

    @Override
    public String getTemplate() {
        return "subTextColumn";
    }

    public SingularAttribute<? super F, ?> getSubattribute() {
        return subattribute;
    }

    public void setSubattribute(SingularAttribute<? super F, ?> subattribute) {
        this.subattribute = subattribute;
    }
    
    public String getSubName(){
        return getSubattribute().getName();
    }
    
}
