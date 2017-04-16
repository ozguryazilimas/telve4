/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.messages.MessagesUtils;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Enum Tipi değerleri gösteren kolon tipi.
 * @author Hakan Uygun
 * @param <E> İşlenecek olan Entity sınıfı
 */
public class EnumColumn<E> extends Column<E> {

    /**
     * Enum Tipleri kolon oluşiturur.
     * @param attribute gösterilecek olan attribute
     * @param labelKey kolon ismi
     * @param keyPrefix enum değerler için ön ek
     */
    public EnumColumn(SingularAttribute<? super E, ? extends Enum> attribute, String labelKey, String keyPrefix) {
        super(attribute, labelKey);
        setKeyPrefix(keyPrefix);
    }

    @Override
    public String getTemplate() {
        return "enumColumn";
    }

    @Override
    public void export(E e, Writer doc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        doc.write("\"");
        String val = BeanUtils.getProperty(e, getName());
        if( !Strings.isNullOrEmpty(val)){
            doc.write( MessagesUtils.getMessage( getKeyPrefix() + val ));
        }
        doc.write("\"");
    }
    
    
}
