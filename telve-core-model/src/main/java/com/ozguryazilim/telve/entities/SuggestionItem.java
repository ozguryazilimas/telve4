/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Suggestion sistemi içinde kullanılacak olan veri modeli.
 *
 * Her kayıt bir öneri kalemidir.
 *
 * @author Hakan Uygun
 */
@Entity
@Table(name = "SUGGESTION_ITEM")
public class SuggestionItem extends EntityBase{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    /**
     * Öneri metni
     */
    @Column(name = "DATA")
    @NotNull
    private String data;

    @Column(name = "INFO")
    private String info;

    @Column(name = "ISACTIVE")
    private Boolean active;

    @Column(name = "CODE_GROUP")
    private String group;

    /**
     * Verilerin kendi aralarında gruplamasını sağlamak bulmayı kolaylaştırmak
     * için ek bilgi alanı.
     *
     * Örneğin Bir şirketin temsilcileri için şirket kodu
     */
    @Column(name = "CODE_KEY")
    private String key;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Geriye Öneri metnini döndürür.
     * @return 
     */
    public String getData() {
        return data;
    }

    /**
     * Öneri Metnini Setler
     * @param data 
     */
    public void setData(String data) {
        this.data = data;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
