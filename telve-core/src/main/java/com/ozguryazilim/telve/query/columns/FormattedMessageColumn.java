/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.columns;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.UserService;
import com.ozguryazilim.telve.messages.FormatedMessage;
import com.ozguryazilim.telve.messages.MessagesUtils;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Formatted Text Out veren column tipi.
 * 
 * @author oyas
 * @param <E> Entitity Sınıfı
 */
public class FormattedMessageColumn<E> extends Column<E>{
	
	private FormatedMessage formattedMessage;

    public FormattedMessageColumn(SingularAttribute<? super E, ?> attribute, String labelKey) {
        super(attribute, labelKey);
    }
    
    @Override
    public String getTemplate() {
        return "formattedMessageColumn";
    }

    
}
