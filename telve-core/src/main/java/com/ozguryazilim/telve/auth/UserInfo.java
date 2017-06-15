/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.io.Serializable;
import java.util.Objects;

/**
 * Kullanıcı login bilgilerinin tutulduğu veri modeli.
 * 
 * Telve uygulamaları içerisinde bu model kullanılır. 
 * 
 * Bu modelin içeriği altta kullanılan Security katmanları tarafından doldurulur.
 * 
 * Mevcut durumda PicketLink Identity tanımından alınmaktadır.
 * 
 * Şimdilik sadece temel bilgiler var.
 * 
 * İleride UserType, Roles v.b. lazım oldukça eklenecek.
 * 
 * @author Hakan Uygun
 */
public class UserInfo implements Serializable{
    
    private String id;
    private String loginName;
    private String firstName;
    private String lastName;
    private String userType;
    private String email;
    private String mobile;
    private Long   domainGroupId;
    private String domainGroupName;
    private String domainGroupPath;

    /**
     * Geriye Kullanıcının ID'sini döndürür.
     * @return 
     */
    public String getId() {
        return id;
    }

    /**
     * Kullanıcı ID bilgisini setler
     * @param id 
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getDomainGroupId() {
        return domainGroupId;
    }

    public void setDomainGroupId(Long domainGroupId) {
        this.domainGroupId = domainGroupId;
    }

    public String getDomainGroupName() {
        return domainGroupName;
    }

    public void setDomainGroupName(String domainGroupName) {
        this.domainGroupName = domainGroupName;
    }

    public String getDomainGroupPath() {
        return domainGroupPath;
    }

    public void setDomainGroupPath(String domainGroupPath) {
        this.domainGroupPath = domainGroupPath;
    }
    
    public String getUserName(){
        return getFirstName() + " " + getLastName();
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserInfo other = (UserInfo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "id=" + id + ", loginName=" + loginName + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }


    
    
}
