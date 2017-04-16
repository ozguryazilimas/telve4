/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import com.google.common.base.Strings;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;

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

    @Override
    public void export(E e, Writer doc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        doc.write("\"");
        try{
            String val = BeanUtils.getNestedProperty(e, getName() + "." + getSubName());
            if( !Strings.isNullOrEmpty(val)){
                doc.write( val );
            }
        } catch ( NestedNullException ex ){
            //NPE geliyorsa boş bırakılacak.
        }
        doc.write("\"");
    }
    
    
    
}
