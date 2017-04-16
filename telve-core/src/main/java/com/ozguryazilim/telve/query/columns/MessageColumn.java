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
