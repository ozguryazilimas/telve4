/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query;

import java.lang.reflect.Member;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

/**
 * Sadece View Model üzerinde bulunan alanların colon olarak eklenebilmesi için SingularAttribute implementasyonu.
 * 
 * Filtrelerde kullanılmamalılar.
 * 
 * 
 * @author Hakan Uygun
 */
public class SingularViewAttribute<X, T> implements SingularAttribute<X, T>{

    
    private String name;

    public SingularViewAttribute(String name) {
        this.name = name;
    }
    
    @Override
    public boolean isId() {
        return false;
    }

    @Override
    public boolean isVersion() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public Type<T> getType() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PersistentAttributeType getPersistentAttributeType() {
        return null;
    }

    @Override
    public ManagedType<X> getDeclaringType() {
        return null;
    }

    @Override
    public Class<T> getJavaType() {
        return null;
    }

    @Override
    public Member getJavaMember() {
        return null;
    }

    @Override
    public boolean isAssociation() {
        return false;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public BindableType getBindableType() {
        return null;
    }

    @Override
    public Class<T> getBindableJavaType() {
        return null;
    }
    
}
