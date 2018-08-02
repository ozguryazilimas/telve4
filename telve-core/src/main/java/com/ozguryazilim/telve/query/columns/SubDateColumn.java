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
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;

/**
 *
 * @author yusuf
 */
public class SubDateColumn<E, D> extends Column<E> {

    private SingularAttribute<? super D, ?> subattribute;

    public SubDateColumn(Attribute<? super E, ?> attribute, SingularAttribute<? super D, ?> subattribute, String labelKey) {
        super(attribute, labelKey);
        this.subattribute = subattribute;
    }

    @Override
    public String getTemplate() {
        return "subDateColumn";
    }

    public SingularAttribute<? super D, ?> getSubattribute() {
        return subattribute;
    }

    public String getSubName() {
        return getSubattribute().getName();
    }

    public void setSubattribute(SingularAttribute<? super D, ?> subattribute) {
        this.subattribute = subattribute;
    }

    @Override
    public void export(E e, Writer doc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        doc.write("\"");
        try {
            String val = BeanUtils.getNestedProperty(e, getName() + "." + getSubName());
            if (!Strings.isNullOrEmpty(val)) {
                doc.write(val);
            }
        } catch (NestedNullException ex) {
            //NPE geliyorsa boş bırakılacak.
        }
        doc.write("\"");
    }

}
