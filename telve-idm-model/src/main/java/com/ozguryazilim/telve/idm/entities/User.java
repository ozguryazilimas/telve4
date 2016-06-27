/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.entities;

import com.ozguryazilim.telve.entities.ParamEntityBase;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Kullanıcı Modeli
 * 
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLI_USER")
public class User extends ParamEntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "FIRST_NAME")
    private String firstName;
    
    @Column(name = "LAST_NAME")
    private String lastName;
    
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "PW_HASH")
    private String passwordEncodedHash;
    @Column(name = "PW_SALT")
    private String passwordSalt;
    
    /**
     * Uygulama tarafından register edilen farklı kullanıcı tipleri.
     */
    @Column(name = "USER_TYPE")
    private String userType;
    
    /**
     * Kullanıcıya ek veri tanım alanları. Extention2lar için
     */
    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "key")
    private Map<String,UserAttribute> attributes = new HashMap<>();

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordEncodedHash() {
        return passwordEncodedHash;
    }

    public void setPasswordEncodedHash(String passwordEncodedHash) {
        this.passwordEncodedHash = passwordEncodedHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Map<String, UserAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, UserAttribute> attributes) {
        this.attributes = attributes;
    }
    
    
    
}
