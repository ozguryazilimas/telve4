/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.columns;

import javax.persistence.metamodel.Attribute;

/**
 * UserLogin bilgisi içeren bir alanı Kullanıcının gerçek adı ile sunar.
 * @author oyas
 */
public class UserColumn<E> extends Column<E> {

    public UserColumn(Attribute<? super E, ?> attribute, String labelKey) {
        super(attribute, labelKey);
    }

    @Override
    public String getTemplate() {
        return "userColumn";
    }
    
}
