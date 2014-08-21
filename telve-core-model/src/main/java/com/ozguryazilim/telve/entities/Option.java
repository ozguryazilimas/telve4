/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Uygulama için tanımlanmış olan seçeneklerin kullanıcı ya da sistem için
 * değerlerini saklar.
 *
 * Aynı seçenek farklı sahipliklerle farklı değerler içerebilir. owner alanında
 * bulunan değere göre karar verilir.
 *
 * Örneğin sistem geneli için geçerli bir seçenek için "SYSTEM" owner'ı telve
 * kullanıcısı için "telve" owner'ı olabilir.
 *
 * Bu durum, System, Kullanıcı, Grup, Role, ProjectState gibi değerler olabilir.
 *
 * @author Hakan Uygun
 */
@Entity
@Table(name = "OPTIONS")
public class Option extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "OP_OWNER", length = 20)
    @Size(max = 20)
    private String owner;

    @Column(name = "OP_KEY", length = 255)
    @Size(max = 255)
    private String key;

    @Column(name = "OP_VALUE")
    private String value;

    public Option() {
    }
    
    public Option(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public Option(String key) {
        this.key = key;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAsString() {
        return value;
    }

    public void setAsString(String val) {
        value = val;
    }

    public Integer getAsInteger() {
        return Integer.parseInt(value);
    }

    public void setAsInteger(Integer val) {
        value = val.toString();
    }

    @SuppressWarnings("deprecation")
    public Date getAsDate() {
        return new Date(value);
    }

    public void setAsDate(Date val) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        value = sdf.format(val);
    }

    public Boolean getAsBoolean() {
        return Boolean.parseBoolean(value);
    }

    public void setAsBoolean(Boolean bool) {
        value = bool.toString();
    }

    public BigDecimal getAsBigDecimal() {
        return new BigDecimal(value);
    }

    public void setAsBigDecimal(BigDecimal aValue) {
        //TODO:scale i 2 mi almalı parametreye mi bağlamalı?
        value = aValue.setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * İstenen enuma çevirme işlemlerini yapar.
     *
     * @param <T> hedef enum tipi
     * @param enumClazz, hedef enum tipi
     * @return enum, çevirimi yapılmış enum.
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum> T getAsEnum(Class<T> enumClazz) {

        T o = null;
        try {
            o = (T) Enum.valueOf(enumClazz, value);
        } catch (Exception e) {
			//verilen değer enumeration içerisinde olmadığından
            //null dönecektir.
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public void setAsEnum(Enum enumVal) {
        value = enumVal.name();
    }

    //TODO: DateTime, Time, v.b. alanlar yazılacak.
}
